package com.remitroserver.api.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record SignUpRequest(
	@Schema(description = "사용자 이메일", example = "member@example.com")
	@Email(message = "이메일 형식에 맞게 입력해주세요.")
	@NotBlank(message = "사용자 이메일을 입력해주세요.")
	String email,

	@Schema(description = "사용자 비밀번호", example = "encodedPassword")
	@NotBlank(message = "사용자 비밀번호를 입력해주세요.")
	String password,

	@Schema(description = "확인용 비밀번호", example = "encodedPassword")
	@NotBlank(message = "확인 비밀번호를 입력해주세요.")
	String checkPassword,

	@Schema(description = "사용자 주민등록번호", example = "111111-1234567")
	@NotBlank(message = "사용자 주민등록번호를 입력해주세요.")
	@Pattern(
		regexp = "^\\d{6}-[1-4]\\d{6}$",
		message = "주민등록번호 형식에 맞게 입력해주세요. (예: 111111-1234567)"
	)
	String registrationNumber,

	@Schema(description = "사용자 닉네임", example = "memberNickname")
	@NotBlank(message = "사용자 닉네임을 입력해주세요.")
	String nickname
) {
}
