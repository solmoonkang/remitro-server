package com.remitro.common.contract;

public record MemberAuthInfo(
	Long memberId,

	String email,

	String nickname,

	String hashedPassword
) {
}
