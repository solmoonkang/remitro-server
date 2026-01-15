package com.remitro.common.exception;

import com.remitro.common.error.ErrorCode;

public class NotFoundException extends BaseException {

	public NotFoundException(ErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}

	public NotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
