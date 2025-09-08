package com.remitro.member.application.mapper;

import com.remitro.member.application.dto.response.TokenResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenMapper {

	public static TokenResponse toTokenResponse(String accessToken, String refreshToken) {
		return new TokenResponse(accessToken, refreshToken);
	}
}
