package com.remitro.support.exception;

import com.remitro.support.error.ErrorCode;

public class NotFoundException extends BaseException {

	public NotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

	public NotFoundException(ErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}
}
