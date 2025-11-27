package com.remitro.common.infrastructure.error.exception;

import com.remitro.common.infrastructure.error.model.ErrorMessage;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

	private final ErrorMessage errorMessage;

	public NotFoundException(ErrorMessage errorMessage, Object... args) {
		super(errorMessage.formattedMessage(args));
		this.errorMessage = errorMessage;
	}
}
