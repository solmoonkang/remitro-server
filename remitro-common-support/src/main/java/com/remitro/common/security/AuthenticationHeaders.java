package com.remitro.common.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationHeaders {

	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER_PREFIX = "Bearer ";

	public static final String X_MEMBER_ID = "X-Member-Id";
	public static final String X_MEMBER_EMAIL = "X-Member-Email";
	public static final String X_MEMBER_NICKNAME = "X-Member-Nickname";
	public static final String X_MEMBER_ROLE = "X-Member-Role";
}
