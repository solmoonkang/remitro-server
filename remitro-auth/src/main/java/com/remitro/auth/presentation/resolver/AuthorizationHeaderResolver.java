package com.remitro.auth.presentation.resolver;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.UnauthorizedException;
import com.remitro.common.error.message.ErrorMessage;

@Component
public class AuthorizationHeaderResolver {

	private static final String BEARER_PREFIX = "Bearer ";

	public String resolve(String authorizationHeader) {
		if (!StringUtils.hasText(authorizationHeader)) {
			throw new UnauthorizedException(
				ErrorCode.TOKEN_INVALID, ErrorMessage.TOKEN_INVALID
			);
		}

		if (!authorizationHeader.startsWith(BEARER_PREFIX)) {
			throw new UnauthorizedException(
				ErrorCode.TOKEN_INVALID, ErrorMessage.TOKEN_INVALID
			);
		}

		return authorizationHeader.substring(BEARER_PREFIX.length());
	}
}
