package com.remitro.member.application.read.account.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "회원 이메일 계정 찾기 요청")
public record EmailFindRequest(
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
