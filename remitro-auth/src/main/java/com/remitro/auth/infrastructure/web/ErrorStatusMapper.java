package com.remitro.auth.infrastructure.web;

import static com.remitro.common.error.code.ErrorCode.*;

import java.util.Set;

import org.springframework.http.HttpStatus;

import com.remitro.common.error.code.ErrorCode;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorStatusMapper {

	private static final Set<ErrorCode> BAD_REQUEST = Set.of(
		PASSWORD_INVALID,
		INVALID_REQUEST
	);

	private static final Set<ErrorCode> UNAUTHORIZED = Set.of(
		TOKEN_INVALID,
		TOKEN_EXPIRED,
		AUTHENTICATION_NOT_ALLOWED
	);

	private static final Set<ErrorCode> FORBIDDEN = Set.of(
		ACCESS_FORBIDDEN
	);

	public static HttpStatus map(ErrorCode errorCode) {
		if (BAD_REQUEST.contains(errorCode)) return HttpStatus.BAD_REQUEST;
		if (UNAUTHORIZED.contains(errorCode)) return HttpStatus.UNAUTHORIZED;
		if (FORBIDDEN.contains(errorCode)) return HttpStatus.FORBIDDEN;
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
