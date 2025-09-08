package com.remitro.common.error.exception;

import com.remitro.common.error.model.ErrorMessage;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {

	private final ErrorMessage errorMessage;

	public UnauthorizedException(ErrorMessage errorMessage, Object... args) {
		super(errorMessage.formattedMessage(args));
		this.errorMessage = errorMessage;
	}
}
