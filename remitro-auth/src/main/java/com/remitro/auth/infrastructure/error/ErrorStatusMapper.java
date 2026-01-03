package com.remitro.auth.infrastructure.error;

import org.springframework.http.HttpStatus;

import com.remitro.common.error.code.ErrorCode;

public final class ErrorStatusMapper {

	private ErrorStatusMapper() {}

	public static HttpStatus map(ErrorCode errorCode) {
		String code = errorCode.getCode();

		if (code.contains("_400_")) return HttpStatus.BAD_REQUEST;
		if (code.contains("_401_")) return HttpStatus.UNAUTHORIZED;
		if (code.contains("_403_")) return HttpStatus.FORBIDDEN;
		if (code.contains("_404_")) return HttpStatus.NOT_FOUND;
		if (code.contains("_409_")) return HttpStatus.CONFLICT;
		if (code.contains("_503_")) return HttpStatus.SERVICE_UNAVAILABLE;

		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
