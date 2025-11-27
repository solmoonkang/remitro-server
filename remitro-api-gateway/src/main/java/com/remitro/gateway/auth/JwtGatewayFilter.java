package com.remitro.gateway.auth;

import static com.remitro.gateway.constant.AuthorizationConstant.*;
import static com.remitro.gateway.constant.GatewayHeaderConstant.*;
import static com.remitro.gateway.constant.GlobalConstant.*;
import static com.remitro.gateway.constant.JwtClaimsConstant.*;

import java.util.Arrays;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtGatewayFilter implements GlobalFilter, Ordered {

	private final JwtProvider jwtProvider;

	@Override
	public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
		ServerHttpRequest serverHttpRequest = serverWebExchange.getRequest();
		final String serverHttpRequestPath = serverHttpRequest.getURI().getPath();

		if (Arrays.stream(WHITE_LIST).anyMatch(serverHttpRequestPath::startsWith)) {
			return gatewayFilterChain.filter(serverWebExchange);
		}

		final String token = jwtProvider.extractToken(serverHttpRequest);
		if (token == null || !jwtProvider.isValidToken(token)) {
			return unauthorized(serverWebExchange);
		}

		final Claims claims = jwtProvider.extractClaims(token);

		ServerHttpRequest mutatedHttpRequest = serverWebExchange.getRequest().mutate()
			.header(HEADER_MEMBER_ID, String.valueOf(claims.get(CLAIM_MEMBER_ID, Long.class)))
			.header(HEADER_MEMBER_EMAIL, claims.get(CLAIM_MEMBER_EMAIL, String.class))
			.header(HEADER_MEMBER_NICKNAME, claims.get(CLAIM_MEMBER_NICKNAME, String.class))
			.build();

		return gatewayFilterChain.filter(serverWebExchange.mutate().request(mutatedHttpRequest).build());
	}

	private Mono<Void> unauthorized(ServerWebExchange serverWebExchange) {
		serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		serverWebExchange.getResponse().getHeaders().add("Content-Type", CONTENT_TYPE_JSON);

		DataBuffer dataBuffer = serverWebExchange.getResponse().bufferFactory()
			.wrap(ERROR_INVALID_TOKEN_JSON.getBytes());

		return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
	}

	@Override
	public int getOrder() {
		return FILTER_ORDER;
	}
}
