package com.remitro.common.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationConstant {

	// Authorization Header
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String BEARER_PREFIX = "Bearer ";

	// JWT Claims
	public static final String CLAIM_MEMBER_ID = "memberId";
	public static final String CLAIM_MEMBER_EMAIL = "memberEmail";
	public static final String CLAIM_MEMBER_NICKNAME = "memberNickname";
	public static final String CLAIM_MEMBER_ROLE = "memberRole";

	// Gateway Headers
	public static final String HEADER_MEMBER_ID = "X-Member-Id";
	public static final String HEADER_MEMBER_EMAIL = "X-Member-Email";
	public static final String HEADER_MEMBER_NICKNAME = "X-Member-Nickname";
	public static final String HEADER_MEMBER_ROLE = "X-Member-Role";
}
