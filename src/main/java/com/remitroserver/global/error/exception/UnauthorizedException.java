package com.remitroserver.global.error.exception;

import com.remitroserver.global.error.model.ErrorMessage;

public class UnauthorizedException extends RemitroServerException {

	public UnauthorizedException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
