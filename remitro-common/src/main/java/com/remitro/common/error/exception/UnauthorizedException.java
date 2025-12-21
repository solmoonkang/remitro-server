package com.remitro.common.error.exception;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.message.ErrorMessage;

public class UnauthorizedException extends BaseException {

	public UnauthorizedException(ErrorCode errorCode, ErrorMessage errorMessage, Object... args) {
		super(errorCode, errorMessage, args);
	}
}
