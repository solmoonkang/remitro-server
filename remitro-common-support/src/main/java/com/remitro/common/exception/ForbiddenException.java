package com.remitro.common.exception;

import com.remitro.common.error.ErrorCode;

public class ForbiddenException extends BaseException {

	public ForbiddenException(ErrorCode errorCode) {
		super(errorCode);
	}

	public ForbiddenException(ErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}
}
