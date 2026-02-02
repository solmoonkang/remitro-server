package com.remitro.support.exception;

import com.remitro.support.error.ErrorCode;

public class InternalServerException extends BaseException {

	public InternalServerException(ErrorCode errorCode) {
		super(errorCode);
	}

	public InternalServerException(ErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}
}
