package com.remitro.common.infra.error.exception;

import com.remitro.common.infra.error.model.ErrorMessage;

import lombok.Getter;

@Getter
public class LockAcquireException extends RuntimeException {

	private final ErrorMessage errorMessage;

	public LockAcquireException(ErrorMessage errorMessage, Object... args) {
		super(errorMessage.formattedMessage(args));
		this.errorMessage = errorMessage;
	}
}
