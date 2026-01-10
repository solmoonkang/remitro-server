package com.remitro.account.infrastructure.web;

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
		MEMBER_STATE_INVALID,
		INVALID_REQUEST
	);

	private static final Set<ErrorCode> UNAUTHORIZED = Set.of(
		MEMBER_LOCKED,
		MEMBER_WITHDRAWN,
		MEMBER_DORMANT
	);

	private static final Set<ErrorCode> NOT_FOUND = Set.of(
		MEMBER_NOT_FOUND
	);

	private static final Set<ErrorCode> CONFLICT = Set.of(
		MEMBER_DUPLICATED_RESOURCE
	);

	public static HttpStatus map(ErrorCode errorCode) {
		if (BAD_REQUEST.contains(errorCode))
			return HttpStatus.BAD_REQUEST;
		if (UNAUTHORIZED.contains(errorCode))
			return HttpStatus.UNAUTHORIZED;
		if (NOT_FOUND.contains(errorCode))
			return HttpStatus.NOT_FOUND;
		if (CONFLICT.contains(errorCode))
			return HttpStatus.CONFLICT;
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
