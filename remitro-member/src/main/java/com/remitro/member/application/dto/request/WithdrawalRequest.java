package com.remitro.member.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "회원 탈퇴 요청")
public record WithdrawalRequest(
	@Schema(
		description = "본인 확인용 비밀번호",
		example = "P@ss0rd!"
	)
	@NotBlank(message = "비밀번호를 입력해주세요.")
	String password
) {
}
