package com.remitro.auth.domain.model;

import lombok.Builder;

@Builder
public record RefreshToken(
	Long memberId,

	String token,

	String deviceId,

	boolean revoked,

	Long expirationTime
) {
}
