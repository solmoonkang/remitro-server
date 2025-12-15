package com.remitro.common.auth;

import com.remitro.common.security.Role;

public record MemberAuthInfo(
	Long memberId,

	String email,

	String nickname,

	String hashedPassword,

	Role role
) {
}
