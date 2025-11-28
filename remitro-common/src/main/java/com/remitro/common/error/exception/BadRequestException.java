package com.remitro.common.error.exception;

import com.remitro.common.error.model.ErrorMessage;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

	private final ErrorMessage errorMessage;

	public BadRequestException(ErrorMessage errorMessage, Object... args) {
		super(errorMessage.formattedMessage(args));
		this.errorMessage = errorMessage;
	}
}
