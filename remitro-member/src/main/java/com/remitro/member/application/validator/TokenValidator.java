package com.remitro.member.application.validator;

import org.springframework.stereotype.Component;

import com.remitro.common.infra.auth.provider.JwtProvider;
import com.remitro.common.infra.error.exception.BadRequestException;
import com.remitro.common.infra.error.exception.UnauthorizedException;
import com.remitro.common.infra.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenValidator {

	private final JwtProvider jwtProvider;

	public void validateTokenFormat(String token) {
		if (!jwtProvider.validateToken(token)) {
			throw new BadRequestException(ErrorMessage.INVALID_TOKEN);
		}
	}

	public void validateTokenExistence(String storedToken, String token) {
		if (!storedToken.equals(token)) {
			throw new UnauthorizedException(ErrorMessage.TOKEN_EXPIRED);
		}
	}
}
