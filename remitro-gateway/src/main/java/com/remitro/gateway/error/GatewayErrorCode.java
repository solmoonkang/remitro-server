package com.remitro.gateway.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GatewayErrorCode {

	UNAUTHORIZED(401, "인증되지 않은 요청입니다."),
	TOO_MANY_REQUESTS(429, "요청이 너무 많습니다."),
	INTERNAL_ERROR(500, "게이트웨이 오류입니다.");

	private final int status;
	private final String message;
}
