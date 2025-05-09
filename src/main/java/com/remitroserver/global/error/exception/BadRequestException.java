package com.remitroserver.global.error.exception;

import com.remitroserver.global.error.model.ErrorMessage;

public class BadRequestException extends RemitroServerException {

	public BadRequestException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}

	public BadRequestException(String message) {
		super(message);
	}
}
