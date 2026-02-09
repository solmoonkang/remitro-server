package com.remitro.member.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "회원 프로필 수정 요청")
public record ProfileUpdateRequest(
	@Schema(
		description = "변경할 닉네임",
		example = "홍길동"
	)
	@NotBlank(message = "닉네임을 입력해주세요.")
	String nickname,

	@Schema(
		description = "변경할 전화번호",
		example = "01012345678"
	)
	@NotBlank(message = "전화번호를 입력해주세요.")
	String phoneNumber
) {
}
