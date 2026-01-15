package com.remitro.common.exception;

import com.remitro.common.error.ErrorCode;

public class UnauthorizedException extends BaseException {

	public UnauthorizedException(ErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}

	public UnauthorizedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
