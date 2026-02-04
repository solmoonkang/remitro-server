package com.remitro.gateway.filter;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.remitro.gateway.error.GatewayErrorCode;
import com.remitro.gateway.error.GatewayException;

import reactor.core.publisher.Mono;

@Component
public class RateLimitFilter implements GlobalFilter, Ordered {

	private static final int MAX_REQUESTS = 100;
	private static final long WINDOW_MS = 60_000;

	private static final String UNKNOWN_CLIENT = "UNKNOWN";

	private final Map<String, Counter> counterMap = new ConcurrentHashMap<>();

	@Override
	public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
		String clientIp = extractClientIp(serverWebExchange);

		Counter counter = counterMap.computeIfAbsent(clientIp, ip -> new Counter());

		if (!counter.allow()) {
			return Mono.error(new GatewayException(GatewayErrorCode.TOO_MANY_REQUESTS));
		}

		return gatewayFilterChain.filter(serverWebExchange);
	}

	private String extractClientIp(ServerWebExchange serverWebExchange) {
		if (serverWebExchange.getRequest().getRemoteAddress() == null) {
			return UNKNOWN_CLIENT;
		}

		InetSocketAddress inetSocketAddress = serverWebExchange.getRequest().getRemoteAddress();
		if (inetSocketAddress.getAddress() == null) {
			return UNKNOWN_CLIENT;
		}

		return inetSocketAddress.getAddress().getHostAddress();
	}

	private static class Counter {
		private int requestCount = 0;
		private long windowStartAt = System.currentTimeMillis();

		synchronized boolean allow() {
			long now = System.currentTimeMillis();

			if (now - windowStartAt > WINDOW_MS) {
				windowStartAt = now;
				requestCount = 0;
			}

			requestCount++;
			return requestCount <= MAX_REQUESTS;
		}
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 2;
	}
}
