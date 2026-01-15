package com.remitro.common.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remitro.common.error.ErrorCode;

import lombok.Builder;

@Builder
public record ErrorResponse(
	String code,

	String message,

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	Map<String, String> validation
) {

	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), null);
	}

	public static ErrorResponse of(String code, String message) {
		return new ErrorResponse(code, message, null);
	}

	public static ErrorResponse of(ErrorCode errorCode, String formattedMessage) {
		return new ErrorResponse(errorCode.getCode(), formattedMessage, null);
	}
}
