package com.remitro.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.remitro.gateway.error.GatewayErrorCode;
import com.remitro.gateway.error.GatewayException;
import com.remitro.gateway.support.AuthProperties;
import com.remitro.gateway.support.TokenResolver;
import com.remitro.gateway.support.TokenValidator;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

	private final AuthProperties authProperties;
	private final TokenResolver tokenResolver;
	private final TokenValidator tokenValidator;

	public AuthenticationFilter(
		AuthProperties authProperties,
		TokenResolver tokenResolver,
		TokenValidator tokenValidator
	) {
		this.authProperties = authProperties;
		this.tokenResolver = tokenResolver;
		this.tokenValidator = tokenValidator;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
		String path = serverWebExchange.getRequest().getURI().getPath();

		if (isWhitelisted(path)) {
			return gatewayFilterChain.filter(serverWebExchange);
		}

		String token = tokenResolver.resolve(serverWebExchange.getRequest());
		if (!tokenValidator.isValid(token)) {
			return Mono.error(new GatewayException(GatewayErrorCode.UNAUTHORIZED));
		}

		return gatewayFilterChain.filter(serverWebExchange);
	}

	private boolean isWhitelisted(String path) {
		return authProperties.whitelist().stream().anyMatch(path::startsWith);
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 1;
	}
}
