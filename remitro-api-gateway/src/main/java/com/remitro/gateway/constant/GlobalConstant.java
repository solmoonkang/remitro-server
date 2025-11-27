package com.remitro.gateway.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GlobalConstant {

	// JSON Content Type
	public static final String CONTENT_TYPE_JSON = "application/json";

	// Error Message
	public static final String ERROR_INVALID_TOKEN_JSON = "{\"message\":\"INVALID_TOKEN\"}";

	// Filter Order
	public static final int FILTER_ORDER = -100;
}
