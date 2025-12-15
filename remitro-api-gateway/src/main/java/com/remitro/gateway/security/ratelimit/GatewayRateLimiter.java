package com.remitro.gateway.security.ratelimit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

@Component
public class GatewayRateLimiter {

	private static final int MAX_REQUESTS = 10;

	private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();

	public boolean allow(String key) {
		requestCounts.putIfAbsent(key, new AtomicInteger(0));
		return requestCounts.get(key).incrementAndGet() <= MAX_REQUESTS;
	}
}
