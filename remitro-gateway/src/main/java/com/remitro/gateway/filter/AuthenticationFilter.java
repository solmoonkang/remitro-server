package com.remitro.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.remitro.gateway.auth.AuthContext;
import com.remitro.gateway.auth.JwtVerifier;
import com.remitro.gateway.auth.TokenResolver;
import com.remitro.gateway.error.GatewayErrorResponseWriter;
import com.remitro.gateway.support.HeaderNames;
import com.remitro.gateway.support.RequestIdGenerator;
import com.remitro.gateway.support.WhiteListPaths;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

	private final TokenResolver tokenResolver;
	private final JwtVerifier jwtVerifier;
	private final GatewayErrorResponseWriter gatewayErrorResponseWriter;

	@Override
	public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
		final String path = serverWebExchange.getRequest().getURI().getPath();

		if (WhiteListPaths.isAllowed(path)) {
			return gatewayFilterChain.filter(serverWebExchange);
		}

		String token = tokenResolver.resolve(serverWebExchange.getRequest());
		if (token == null) {
			return gatewayErrorResponseWriter.unauthorized(serverWebExchange);
		}

		AuthContext authContext;
		try {
			authContext = jwtVerifier.verify(token);
		} catch (Exception e) {
			return gatewayErrorResponseWriter.unauthorized(serverWebExchange);
		}

		ServerHttpRequest request = serverWebExchange.getRequest().mutate()
			.header(HeaderNames.MEMBER_ID, authContext.memberId().toString())
			.header(HeaderNames.MEMBER_ROLE, authContext.role().name())
			.header(HeaderNames.REQUEST_ID, RequestIdGenerator.generate())
			.build();

		return gatewayFilterChain.filter(serverWebExchange.mutate().request(request).build());
	}

	@Override
	public int getOrder() {
		return -100;
	}
}
