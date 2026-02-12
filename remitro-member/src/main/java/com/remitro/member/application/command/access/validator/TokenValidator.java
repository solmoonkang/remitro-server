package com.remitro.member.application.command.access.validator;

import org.springframework.stereotype.Component;

import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.UnauthorizedException;
import com.remitro.member.domain.token.model.RefreshToken;
import com.remitro.member.domain.token.policy.RefreshTokenPolicy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenValidator {

	private final RefreshTokenPolicy refreshTokenPolicy;

	public void validateTokenMatch(RefreshToken storedToken, String requestToken) {
		if (!refreshTokenPolicy.matches(storedToken, requestToken)) {
			throw new UnauthorizedException(ErrorCode.INVALID_TOKEN, "인증 정보");
		}
	}
}
