package com.remitro.member.application.command.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "인증 코드 검증 요청")
public record CodeVerifyRequest(
	@Schema(
		description = "인증 코드를 받은 이메일 주소",
		example = "testEmail@example.com"
	)
	@NotBlank(message = "이메일 주소를 입력해주세요.")
	@Email(message = "유효한 이메일 형식이어야 합니다.")
	String email,

	@Schema(
		description = "이메일로 발송된 6자리 인증 코드",
		example = "A1B2C3"
	)
	@NotBlank(message = "인증 코드를 입력해주세요.")
	@Pattern(
		regexp = "^[A-Z0-9]{6}$",
		message = "인증 코드는 영대문자와 숫자를 조합한 6자리여야 합니다."
	)
	String verificationCode
) {
}
