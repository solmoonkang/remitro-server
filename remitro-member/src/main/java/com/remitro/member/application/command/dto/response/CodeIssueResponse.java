package com.remitro.member.application.command.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "인증 번호 발급 응답")
public record CodeIssueResponse(
	@Schema(
		description = "발급된 6자리 인증 번호",
		example = "A1B2C3"
	)
	String verificationCode
) {
}
