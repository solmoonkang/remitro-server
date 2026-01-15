package com.remitro.common.exception;

import com.remitro.common.error.ErrorCode;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

	private final ErrorCode errorCode;

	public BaseException(ErrorCode errorCode, Object... args) {
		super(errorCode.formatted(args));
		this.errorCode = errorCode;
	}

	public BaseException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
