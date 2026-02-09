package com.remitro.member.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "인증 성공 확정 응답")
public record CodeConfirmResponse(
	@Schema(
		description = "인증 성공 증표 (일회용 UUID 토큰)",
		example = "550e8400-e29b-41d4-a716-446655440000"
	)
	String verificationToken
) {
}
