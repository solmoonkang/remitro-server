package com.remitro.gateway.auth;

import com.remitro.common.security.Role;

public record AuthContext(
	Long memberId,

	Role role
) {
}
