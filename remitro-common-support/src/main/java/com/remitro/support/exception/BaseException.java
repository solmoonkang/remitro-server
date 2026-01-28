package com.remitro.support.exception;

import com.remitro.support.error.ErrorCode;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

	private final ErrorCode errorCode;

	protected BaseException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	protected BaseException(ErrorCode errorCode, Object... args) {
		super(errorCode.formatted(args));
		this.errorCode = errorCode;
	}
}
