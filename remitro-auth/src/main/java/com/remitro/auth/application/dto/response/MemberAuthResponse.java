package com.remitro.auth.application.dto.response;

import lombok.Builder;

@Builder
public record MemberAuthResponse(
	Long memberId,

	String email,

	String nickname,

	String hashedPassword
) {
}
