package com.remitro.common.error.exception;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.message.ErrorMessage;

public class NotFoundException extends BaseException {

	public NotFoundException(ErrorCode errorCode, ErrorMessage errorMessage, Object... args) {
		super(errorCode, errorMessage, args);
	}
}
