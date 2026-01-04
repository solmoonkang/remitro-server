package com.remitro.common.error.exception;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.message.ErrorMessage;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

	private final ErrorCode errorCode;

	protected BaseException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	protected BaseException(ErrorCode errorCode, ErrorMessage errorMessage, Object... args) {
		super(errorMessage.formattedMessage(args));
		this.errorCode = errorCode;
	}
}
