package com.remitro.common.error.exception;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.message.ErrorMessage;

public class ForbiddenException extends BaseException {

	public ForbiddenException(ErrorCode errorCode, ErrorMessage errorMessage, Object... args) {
		super(errorCode, errorMessage, args);
	}
}
