package com.remitro.common.exception;

import com.remitro.common.error.ErrorCode;

public class ConflictException extends BaseException {

	public ConflictException(ErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}

	public ConflictException(ErrorCode errorCode) {
		super(errorCode);
	}
}
