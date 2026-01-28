package com.remitro.support.exception;

import com.remitro.support.error.ErrorCode;

public class ForbiddenException extends BaseException {

	public ForbiddenException(ErrorCode errorCode) {
		super(errorCode);
	}

	public ForbiddenException(ErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}
}
