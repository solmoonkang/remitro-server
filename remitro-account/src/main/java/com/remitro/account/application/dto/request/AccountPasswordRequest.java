package com.remitro.account.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AccountPasswordRequest(
	@NotBlank(message = "계좌 비밀번호를 입력해주세요.")
	String password
) {
}
