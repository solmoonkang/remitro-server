package com.remitro.member.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "비밀번호 복구 요청")
public record PasswordRecoveryRequest(
	@Schema(
		description = "이메일",
		example = "testEmail@example.com"
	)
	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "유효한 이메일 형식이어야 합니다.")
	String email,

	@Schema(
		description = "인증 성공 증표 (일회용 UUID 토큰)",
		example = "550e8400-e29b-41d4-a716-446655440000"
	)
	@NotBlank(message = "인증 증표가 누락되었습니다.")
	String verificationToken,

	@Schema(
		description = "새 비밀번호",
		example = "newP@ss0rd!"
	)
	@Size(min = 8, max = 20, message = "비밀번호는 8~20자 사이여야 합니다.")
	@NotBlank(message = "새 비밀번호를 입력해주세요.")
	String newPassword,

	@Schema(
		description = "새 비밀번호 확인",
		example = "newP@ss0rd!"
	)
	@NotBlank(message = "확인 비밀번호를 입력해주세요.")
	String confirmPassword
) {
}
