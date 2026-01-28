package com.remitro.support.exception;

import com.remitro.support.error.ErrorCode;

public class BadRequestException extends BaseException {

	public BadRequestException(ErrorCode errorCode) {
		super(errorCode);
	}

	public BadRequestException(ErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}
}
