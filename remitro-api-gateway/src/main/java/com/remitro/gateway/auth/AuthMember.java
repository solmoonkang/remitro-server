package com.remitro.gateway.auth;

public record AuthMember(
	Long memberId,

	String email,

	String nickname
) {
}
