package com.remitroserver.global.error.exception;

import com.remitroserver.global.error.model.ErrorMessage;

public class NotFoundException extends RemitroServerException {

	public NotFoundException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
