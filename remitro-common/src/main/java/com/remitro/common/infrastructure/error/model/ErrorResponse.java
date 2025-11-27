package com.remitro.common.infrastructure.error.model;

import java.util.Map;


public record ErrorResponse(
	String message,
	@JsonInclude(JsonInclude.Include.NON_EMPTY) Map<String, String> validation
) {
}
