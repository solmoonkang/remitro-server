package com.remitro.member.application.command.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "인증 코드 발송 요청")
public record CodeSendRequest(
	@Schema(
		description = "인증 코드를 받을 이메일 주소",
		example = "testEmail@example.com"
	)
	@NotBlank(message = "이메일 주소를 입력해주세요.")
	@Email(message = "유효한 이메일 형식이어야 합니다.")
	String email
) {
}
