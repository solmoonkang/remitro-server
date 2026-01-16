package com.remitro.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	INVALID_INPUT_VALUE(400, "COMMON_400_001", "%s는 잘못된 입력 값입니다."),

	MEMBER_NOT_FOUND(404, "MEMBER_404_001", "존재하지 않는 회원입니다."),

	METHOD_NOT_ALLOWED(405, "COMMON_405_001", "허용되지 않은 HTTP 메소드입니다."),

	DUPLICATE_EMAIL(409, "MEMBER_409_001", "%s는 이미 사용 중인 이메일입니다."),
	DUPLICATE_NICKNAME(409, "MEMBER_409_002", "%s는 이미 사용 중인 닉네임입니다."),
	DUPLICATE_PHONE_NUMBER(409, "MEMBER_409_003", "%s는 이미 등록된 전화번호입니다."),

	INTERNAL_SERVER_ERROR(500, "COMMON_500_001", "서버 내부 오류가 발생했습니다.");

	private final int status;
	private final String code;
	private final String message;

	public String formatted(Object... args) {
		return String.format(this.message, args);
	}
}
