package com.remitro.gateway.auth;

import org.apache.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TokenResolver {

	public String resolve(ServerHttpRequest serverHttpRequest) {
		final String bearer = serverHttpRequest.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

		if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
			return bearer.substring(7);
		}

		return null;
	}
}
