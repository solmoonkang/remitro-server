package com.remitro.gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import com.remitro.gateway.support.AuthProperties;
import com.remitro.gateway.support.TokenResolver;
import com.remitro.gateway.support.TokenValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RouteConfig {

	private static final String HEADER_MEMBER_ID = "X-MEMBER-ID";
	private static final String HEADER_ROLE = "X-ROLE";

	private final TokenResolver tokenResolver;
	private final TokenValidator tokenValidator;
	private final AuthProperties authProperties;

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()

			.route("member-service", routeSpec -> routeSpec
				.path("/api/v1/members/**", "/api/v1/auth/**")
				.filters(filterSpec -> filterSpec.filter(this::authenticate))
				.uri("lb://remitro-member")
			)

			.route("account-service", routeSpec -> routeSpec
				.path("/api/v1/accounts/**")
				.filters(filterSpec -> filterSpec.filter(this::authenticate))
				.uri("lb://remitro-account")
			)

			.route("transaction-service", routeSpec -> routeSpec
				.path("/api/v1/transactions/**")
				.filters(filterSpec -> filterSpec.filter(this::authenticate))
				.uri("lb://remitro-transaction")
			)

			.build();
	}

	private Mono<Void> authenticate(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
		final ServerHttpRequest serverHttpRequest = serverWebExchange.getRequest();
		final String path = serverHttpRequest.getURI().getPath();

		log.info("[✅ LOGGER] 게이트웨이 요청이 들어왔습니다. (PATH = {})",
			serverWebExchange.getRequest().getURI().getPath()
		);

		if (isWhitelisted(path)) {
			return gatewayFilterChain.filter(serverWebExchange);
		}

		final String token = tokenResolver.resolve(serverHttpRequest);
		log.info("[✅ LOGGER] 게이트웨이 토큰 존재 여부를 파악합니다. (TOKEN_EXISTS = {})", token != null);

		if (!tokenValidator.isValid(token)) {
			log.warn("[✅ LOGGER] 게이트웨이 인증에 유효하지 않은 토큰입니다.");
			serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return serverWebExchange.getResponse().setComplete();
		}

		final Long memberId = tokenValidator.extractMemberId(token);
		final String role = tokenValidator.extractRole(token);

		log.info("[✅ LOGGER] 게이트웨이 인증에 성공했습니다. (MEMBER_ID = {}, ROLE = {})",
			memberId, role
		);

		final ServerHttpRequest authenticatedHttpRequest = withAuthenticationHeaders(serverHttpRequest, memberId, role);
		return gatewayFilterChain.filter(serverWebExchange.mutate().request(authenticatedHttpRequest).build());
	}

	private boolean isWhitelisted(String path) {
		return authProperties.whitelist().stream().anyMatch(path::startsWith);
	}

	private ServerHttpRequest withAuthenticationHeaders(ServerHttpRequest originalRequest, Long memberId, String role) {
		return originalRequest.mutate()
			.headers(httpHeaders -> {
				// 1. 기존 Authorization 헤더 제거 (수정 가능한 HttpHeaders 객체임)
				httpHeaders.remove(HttpHeaders.AUTHORIZATION);

				// 2. 신규 인증 헤더 추가
				httpHeaders.add(HEADER_MEMBER_ID, String.valueOf(memberId));
				httpHeaders.add(HEADER_ROLE, role);
			})
			.build();
	}
}
