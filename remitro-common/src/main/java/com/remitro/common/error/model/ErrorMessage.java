package com.remitro.common.error.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorMessage {

	// 400: BAD REQUEST
	INVALID_PASSWORD("입력하신 비밀번호는 틀린 비밀번호입니다."),

	// 401: UNAUTHORIZED
	TOKEN_EXPIRED("이미 만료된 토큰입니다."),

	// 403: FORBIDDEN

	// 404: NOT FOUND

	// 409: CONFLICT
	EMAIL_DUPLICATED("이미 사용중인 이메일입니다."),
	NICKNAME_DUPLICATED("이미 사용중인 닉네임입니다."),

	// 500: INTERNAL SERVER ERROR
	UNKNOWN_SERVER_ERROR("서버에서 알 수 없는 에러가 발생했습니다.");

	private final String message;

	public String formattedMessage(Object... args) {
		return String.format(this.message, args);
	}
}
