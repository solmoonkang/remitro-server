package com.remitro.member.application.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 이메일 계정 찾기 응답")
public record EmailFindResponse(
	@Schema(
		description = "마스킹된 이메일",
		example = "tes***@example.com"
	)
	String email,

	@Schema(
		description = "계정 가입 일시",
		example = "2023-12-25T14:30:00"
	)
	LocalDateTime createdAt
) {
}
