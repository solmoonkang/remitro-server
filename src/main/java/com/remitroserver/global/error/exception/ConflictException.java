package com.remitroserver.global.error.exception;

import com.remitroserver.global.error.model.ErrorMessage;

public class ConflictException extends RemitroServerException {

	public ConflictException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
