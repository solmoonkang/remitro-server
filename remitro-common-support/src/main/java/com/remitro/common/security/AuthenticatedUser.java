package com.remitro.common.security;

public record AuthenticatedUser(
	Long memberId,

	Role role
) {

	public static AuthenticatedUser of(Long memberId, Role role) {
		return new AuthenticatedUser(memberId, role);
	}
}
