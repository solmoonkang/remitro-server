package com.remitro.common.error.exception;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.message.ErrorMessage;

import lombok.Getter;

public class BadRequestException extends BaseException {

	public BadRequestException(ErrorCode errorCode, ErrorMessage errorMessage, Object... args) {
		super(errorCode, errorMessage, args);
	}
}
