package com.remitro.support.exception;

import com.remitro.support.error.ErrorCode;

public class UnauthorizedException extends BaseException {

	public UnauthorizedException(ErrorCode errorCode) {
		super(errorCode);
	}

	public UnauthorizedException(ErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}
}
