package com.remitro.member.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginRequest(
	@NotBlank(message = "사용자 이메일을 입력해주세요.")
	String email,

	@NotBlank(message = "사용자 비밀번호를 입력해주세요.")
	String password
) {
}
