package com.remitro.gateway.security.ratelimit;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RateLimitPaths {

	private static final List<String> PATHS = List.of(
		"/auth/login",
		"/auth/reissue"
	);

	public static boolean isTarget(String path) {
		return PATHS.stream().anyMatch(path::startsWith);
	}
}
