package com.remitroserver.api.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginRequest(
	@Schema(description = "사용자 이메일", example = "member@example.com")
	@Email(message = "이메일 형식에 맞게 입력해주세요.")
	@NotBlank(message = "사용자 이메일을 입력해주세요.")
	String email,

	@Schema(description = "사용자 비밀번호", example = "encodedPassword")
	@NotBlank(message = "사용자 비밀번호를 입력해주세요.")
	String password
) {
}
