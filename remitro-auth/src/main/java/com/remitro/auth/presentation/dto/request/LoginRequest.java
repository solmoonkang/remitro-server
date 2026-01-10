package com.remitro.auth.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(name = "LoginRequest", description = "로그인 요청 DTO")
public record LoginRequest(
	@NotBlank(message = "사용자 이메일을 입력해주세요.")
	@Schema(name = "사용자 이메일", example = "member@example.com")
	String email,

	@NotBlank(message = "사용자 비밀번호를 입력해주세요.")
	@Schema(name = "사용자 비밀번호", example = "memberPassword")
	String password
) {
}
