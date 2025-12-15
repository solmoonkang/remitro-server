package com.remitro.common.security;

public record AuthenticatedUser(
	Long memberId,

	Role role
) {
}
