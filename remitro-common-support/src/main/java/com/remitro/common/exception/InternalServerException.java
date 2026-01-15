package com.remitro.common.exception;

import com.remitro.common.error.ErrorCode;

public class InternalServerException extends BaseException {

	public InternalServerException(ErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}

	public InternalServerException(ErrorCode errorCode) {
		super(errorCode);
	}
}
