package com.remitro.member.application.mapper;

import com.remitro.member.application.command.dto.response.TokenResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class TokenMapper {

	public static TokenResponse toLoginResponse(String accessToken, String refreshToken) {
		return new TokenResponse(accessToken, refreshToken);
	}
}
