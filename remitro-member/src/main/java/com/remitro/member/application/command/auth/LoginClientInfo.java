package com.remitro.member.application.command.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "로그인 이력 정보")
public record LoginClientInfo(
	@Schema(
		description = "클라이언트 IP 주소",
		example = "127.0.0.1"
	)
	@NotBlank(message = "클라이언트 IP 주소를 입력해주세요.")
	String clientIp,

	@Schema(
		description = "클라이언트 기기 정보",
		example = "Mozilla/5.0..."
	)
	@NotBlank(message = "클라이언트 기기 정보를 입력해주세요.")
	String userAgent
) {
}
