package com.remitro.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	INVALID_INPUT_VALUE(400, "COMMON_400_001", "%s는 잘못된 입력 값입니다."),

	METHOD_NOT_ALLOWED(405, "COMMON_405_001", "허용되지 않은 HTTP 메소드입니다."),

	INTERNAL_SERVER_ERROR(500, "COMMON_500_001", "서버 내부 오류가 발생했습니다.");

	private final int status;
	private final String code;
	private final String message;

	public String formatted(Object... args) {
		return String.format(this.message, args);
	}
}
