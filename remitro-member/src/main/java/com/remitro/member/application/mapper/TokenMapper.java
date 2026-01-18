package com.remitro.member.application.mapper;

import com.remitro.member.application.command.dto.response.LoginResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenMapper {

	public static LoginResponse toLoginResponse(String accessToken, String refreshToken) {
		return new LoginResponse(accessToken, refreshToken);
	}
}
