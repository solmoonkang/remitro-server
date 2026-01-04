package com.remitro.common.auth;

import com.remitro.common.security.Role;

public record AuthenticatedUser(
	Long memberId,

	Role role
) {
}
