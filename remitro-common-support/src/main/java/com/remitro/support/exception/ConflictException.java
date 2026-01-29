package com.remitro.support.exception;

import com.remitro.support.error.ErrorCode;

public class ConflictException extends BaseException {

	public ConflictException(ErrorCode errorCode) {
		super(errorCode);
	}

	public ConflictException(ErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}
}
