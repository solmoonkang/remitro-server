package com.remitro.common.infrastructure.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtClaimsConstant {

	public static final String CLAIM_MEMBER_ID = "memberId";
	public static final String CLAIM_MEMBER_EMAIL = "memberEmail";
	public static final String CLAIM_MEMBER_NICKNAME = "memberNickname";
}
