package com.remitro.common.contract.member;

public record MemberCredentialsResponse(
	Long memberId,

	String email,

	String nickname,

	String hashedPassword
) {
}
