package com.remitro.member.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "인증 토큰 응답")
public record TokenResponse(
	@Schema(
		description = "액세스 토큰",
		example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxM..."
	)
	String accessToken,

	@Schema(
		description = "리프레시 토큰",
		example = "eXBlIjoiUkVGUkVTSCIsImlhdCI6MTcwMDA..."
	)
	String refreshToken
) {
}
