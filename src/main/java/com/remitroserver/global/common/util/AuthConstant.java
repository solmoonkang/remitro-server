package com.remitroserver.global.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConstant {

	public static final String[] PUBLIC_API_PATHS = {
		"/api/members/signup",
		"/api/auth/login",
		"/api/auth/reissue"
	};

	public static final String[] SECURITY_IGNORED_URLS = {
		"/h2-console/**",
		"/swagger-ui/**",
		"/swagger-resources/**",
		"/v3/api-docs/**"
	};
}
