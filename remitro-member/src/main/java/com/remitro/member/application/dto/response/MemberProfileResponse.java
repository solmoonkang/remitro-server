package com.remitro.member.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 프로필 정보 응답")
public record MemberProfileResponse(
	@Schema(
		description = "마스킹된 이메일",
		example = "tes***@example.com"
	)
	String email,

	@Schema(
		description = "닉네임",
		example = "홍길동"
	)
	String nickname,

	@Schema(
		description = "마스킹된 전화번호",
		example = "010-****-5678"
	)
	String phoneNumber
) {
}
