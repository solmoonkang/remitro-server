package com.remitro.common.error.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remitro.common.error.exception.BaseException;

public record ErrorResponse(
	String code,

	String message,

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	Map<String, String> validation
) {

	public static ErrorResponse from(BaseException e) {
		return new ErrorResponse(e.getErrorCode().getCode(), e.getMessage(), null);
	}
}
