package com.remitro.account.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "계좌 비밀번호 요청 DTO")
public record AccountPasswordRequest(
	@NotBlank(message = "계좌 비밀번호를 입력해주세요.")
	@Schema(description = "계좌 비밀번호", example = "accountPassword")
	String password
) {
}
