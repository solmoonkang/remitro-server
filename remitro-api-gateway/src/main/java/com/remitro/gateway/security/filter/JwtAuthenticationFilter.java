package com.remitro.gateway.security.filter;

import static com.remitro.common.security.AuthenticationConstant.*;
import static com.remitro.gateway.web.WebConstant.*;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.remitro.common.security.Role;
import com.remitro.gateway.security.ratelimit.RateLimitPaths;
import com.remitro.gateway.web.request.RequestIdGenerator;
import com.remitro.gateway.security.ratelimit.GatewayRateLimiter;
import com.remitro.gateway.security.authorization.GatewayAuthorizationManager;
import com.remitro.gateway.security.jwt.JwtClaimsExtractor;
import com.remitro.gateway.security.jwt.JwtProvider;
import com.remitro.gateway.security.whitelist.WhiteListPaths;
import com.remitro.gateway.web.GatewayErrorResponseWriter;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

	private final JwtProvider jwtProvider;
	private final JwtClaimsExtractor jwtClaimsExtractor;
	private final GatewayAuthorizationManager gatewayAuthorizationManager;
	private final GatewayErrorResponseWriter gatewayErrorResponseWriter;
	private final GatewayRateLimiter gatewayRateLimiter;

	@Override
	public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
		ServerHttpRequest originalHttpRequest = serverWebExchange.getRequest();
		final String path = originalHttpRequest.getURI().getPath();

		if (WhiteListPaths.isAllowed(path)) {
			return gatewayFilterChain.filter(serverWebExchange);
		}

		ServerHttpRequest sanitizedHttpRequest = originalHttpRequest.mutate()
			.headers(httpHeaders -> {
				httpHeaders.remove(HEADER_MEMBER_ID);
				httpHeaders.remove(HEADER_MEMBER_ROLE);
				httpHeaders.remove(HEADER_MEMBER_EMAIL);
				httpHeaders.remove(HEADER_MEMBER_NICKNAME);
			})
			.build();

		final String token = jwtProvider.extractToken(sanitizedHttpRequest);
		if (token == null || !jwtProvider.isValidToken(token)) {
			log.warn("[✅ LOGGER] GATEWAY 인증에 실패했습니다: PATH={}, REASON=INVALID_TOKEN",
				path
			);
			return gatewayErrorResponseWriter.unauthorized(serverWebExchange);
		}

		final Claims claims = jwtClaimsExtractor.extract(token, jwtProvider.getSecretKey());
		final Long memberId = jwtClaimsExtractor.extractMemberId(claims);
		final Role role = jwtClaimsExtractor.extractRole(claims);

		try {
			gatewayAuthorizationManager.authorize(path, originalHttpRequest.getMethod(), role);

		} catch (AccessDeniedException e) {
			log.warn("[✅ LOGGER] GATEWAY 경로 및 권한 부족으로 인해 접근이 거부되었습니다: PATH={}, ROLE={}, REASON=FORBIDDEN",
				path, role
			);
			return gatewayErrorResponseWriter.forbidden(serverWebExchange);
		}

		if (RateLimitPaths.isTarget(path)) {
			String ip = originalHttpRequest.getRemoteAddress().getAddress().getHostAddress();
			if (!gatewayRateLimiter.allow(ip)) {
				log.warn("[✅ LOGGER] GATEWAY RATE LIMIT를 초과했습니다: PATH={}, IP={}",
					path, ip
				);
				return gatewayErrorResponseWriter.forbidden(serverWebExchange);
			}
		}

		final String requestId = RequestIdGenerator.generate();

		ServerHttpRequest authenticatedHttpRequest = sanitizedHttpRequest.mutate()
			.header(HEADER_MEMBER_ID, String.valueOf(memberId))
			.header(HEADER_MEMBER_ROLE, role.name())
			.header("X-Request-Id", requestId)
			.build();

		log.info("[✅ LOGGER] GATEWAY 요청이 처리되었습니다: REQUEST_ID={}, PATH={}, ROLE={}",
			requestId, path, role
		);

		return gatewayFilterChain.filter(
			serverWebExchange.mutate().request(authenticatedHttpRequest).build()
		);
	}

	@Override
	public int getOrder() {
		return FILTER_ORDER;
	}
}
