package com.remitro.member.application.dto.response;

import lombok.Builder;

@Builder
public record TokenResponse(
	String accessToken,

	String refreshToken
) {
}
