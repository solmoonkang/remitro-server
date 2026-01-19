package com.remitro.common.security;

public record AuthenticatedUser(
	Long memberId
) {

	public static AuthenticatedUser of(Long memberId) {
		return new AuthenticatedUser(memberId);
	}
}
