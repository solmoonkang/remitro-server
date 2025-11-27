package com.remitro.gateway.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorizationConstant {

	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String BEARER_PREFIX = "Bearer ";

	public static final String[] WHITE_LIST = {
		"/auth/login",
		"/auth/signup",
		"/v3/api-docs",
		"/swagger-ui",
		"/swagger-resources"
	};
}
