package com.remitro.member.application.command.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "회원가입 요청")
public record SignUpRequest(
	@Schema(
		description = "이메일",
		example = "member@example.com"
	)
	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "올바른 이메일 형식을 입력해주세요.")
	String email,

	@Schema(
		description = "비밀번호",
		example = "P@ss0rd!"
	)
	@Size(min = 8, max = 20, message = "비밀번호는 최소 8자 이상 20자 이하로 입력해주세요.")
	@NotBlank(message = "비밀번호를 입력해주세요.")
	String password,

	@Schema(
		description = "닉네임",
		example = "홍길동"
	)
	@NotBlank(message = "닉네임을 입력해주세요.")
	String nickname,

	@Schema(
		description = "전화번호",
		example = "01012345678"
	)
	@NotBlank(message = "전화번호를 입력해주세요.")
	String phoneNumber
) {
}
