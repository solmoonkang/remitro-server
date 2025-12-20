package com.remitro.auth.application.service;

import org.springframework.stereotype.Component;

import com.remitro.auth.infrastructure.security.JwtProvider;
import com.remitro.common.auth.MemberAuthInfo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenIssuer {

	private final JwtProvider jwtProvider;

	public String issueAccessToken(MemberAuthInfo memberAuthInfo) {
		return jwtProvider.generateAccessToken(
			memberAuthInfo.memberId(),
			memberAuthInfo.email(),
			memberAuthInfo.nickname(),
			memberAuthInfo.role()
		);
	}

	public String issueRefreshToken(MemberAuthInfo memberAuthInfo) {
		return jwtProvider.generateRefreshToken(
			memberAuthInfo.memberId(),
			memberAuthInfo.email()
		);
	}
}
