package com.remitro.gateway.security.whitelist;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WhiteListPaths {

	private static final List<String> PATHS = List.of(
		"/auth/login",
		"/auth/signup",
		"/v3/api-docs",
		"/swagger-ui",
		"/swagger-resources"
	);

	public static boolean isAllowed(String path) {
		return PATHS.stream().anyMatch(path::startsWith);
	}
}
