package com.remitro.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.remitro.gateway.support.AuthProperties;
import com.remitro.gateway.support.TokenResolver;
import com.remitro.gateway.support.TokenValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GatewayFilter, Ordered {

	private static final String HEADER_MEMBER_ID = "X-MEMBER-ID";
	private static final String HEADER_ROLE = "X-ROLE";

	private final TokenResolver tokenResolver;
	private final TokenValidator tokenValidator;
	private final AuthProperties authProperties;

	@Override
	public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
		final ServerHttpRequest serverHttpRequest = serverWebExchange.getRequest();
		final String path = serverHttpRequest.getURI().getPath();

		log.info("[✅ LOGGER] 게이트웨이 요청이 들어왔습니다. (PATH = {})", serverWebExchange.getRequest().getURI().getPath());

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

		log.info("[✅ LOGGER] 게이트웨이 인증에 성공했습니다. (MEMBER_ID = {}, ROLE = {})", memberId, role);

		ServerWebExchange authenticatedWebExchange = withAuthenticationHeaders(serverWebExchange, memberId, role);
		return gatewayFilterChain.filter(authenticatedWebExchange);
	}

	private boolean isWhitelisted(String path) {
		return authProperties.whitelist().stream().anyMatch(path::startsWith);
	}

	private ServerWebExchange withAuthenticationHeaders(
		ServerWebExchange serverWebExchange,
		Long memberId,
		String role
	) {
		ServerHttpRequest orignalHttpRequest = serverWebExchange.getRequest();

		ServerHttpRequest decoratedHttpRequest = new ServerHttpRequestDecorator(orignalHttpRequest) {

			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders newHttpHeaders = new HttpHeaders();
				newHttpHeaders.putAll(super.getHeaders());

				newHttpHeaders.remove(HEADER_MEMBER_ID);
				newHttpHeaders.remove(HEADER_ROLE);

				newHttpHeaders.set(HEADER_MEMBER_ID, String.valueOf(memberId));
				newHttpHeaders.set(HEADER_ROLE, role);

				return newHttpHeaders;
			}
		};

		return serverWebExchange.mutate().request(decoratedHttpRequest).build();
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 10;
	}
}
