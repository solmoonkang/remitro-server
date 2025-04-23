package com.remitroserver.global.error.exception;

import com.remitroserver.global.error.model.ErrorMessage;

public class ForbiddenException extends RemitroServerException {

	public ForbiddenException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
