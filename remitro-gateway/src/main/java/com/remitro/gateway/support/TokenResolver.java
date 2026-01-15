package com.remitro.gateway.support;

import org.apache.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class TokenResolver {

	private static final String BEARER = "Bearer ";

	public String resolve(ServerHttpRequest serverHttpRequest) {
		String header = serverHttpRequest.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (header == null || !header.startsWith(BEARER)) {
			return null;
		}
		return header.substring(7);
	}
}
