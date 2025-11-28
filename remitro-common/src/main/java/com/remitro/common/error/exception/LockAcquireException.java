package com.remitro.common.error.exception;

import com.remitro.common.error.model.ErrorMessage;

import lombok.Getter;

@Getter
public class LockAcquireException extends RuntimeException {

	private final ErrorMessage errorMessage;

	public LockAcquireException(ErrorMessage errorMessage, Object... args) {
		super(errorMessage.formattedMessage(args));
		this.errorMessage = errorMessage;
	}
}
