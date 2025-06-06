package com.remitroserver.global.common.util;

import java.time.Duration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtConstant {

	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
	public static final String BEARER_TYPE = "Bearer ";

	public static final String CLAIM_EMAIL = "email";
	public static final String CLAIM_NICKNAME = "nickname";

	public static final Duration REFRESH_TOKEN_EXPIRATION = Duration.ofDays(7);
}
