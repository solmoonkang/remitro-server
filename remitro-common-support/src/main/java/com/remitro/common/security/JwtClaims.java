package com.remitro.common.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtClaims {

	public static final String MEMBER_ID = "memberId";
	public static final String MEMBER_EMAIL = "memberEmail";
	public static final String MEMBER_NICKNAME = "memberNickname";
	public static final String MEMBER_ROLE = "memberRole";
}
