package com.remitro.common.exception;

import com.remitro.common.error.ErrorCode;

public class BadRequestException extends BaseException {

	public BadRequestException(ErrorCode errorCode) {
		super(errorCode);
	}

	public BadRequestException(ErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}
}
