package com.remitro.gateway.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GatewayErrorCode {

	API_NOT_FOUND(404, "요청한 API를 찾을 수 없습니다."),
	METHOD_NOT_ALLOWED(405, "허용되지 않은 HTTP 메서드입니다."),
	UNAUTHORIZED(401, "인증이 필요합니다."),
	TOO_MANY_REQUESTS(429, "요청이 너무 많습니다. 잠시 후 다시 시도해주세요."),

	DOWNSTREAM_UNAVAILABLE(503, "현재 서비스를 이용할 수 없습니다."),
	INTERNAL_ERROR(500, "게이트웨이 내부 오류가 발생했습니다.");

	private final int status;
	private final String message;
}
