package com.remitroserver.global.error.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorMessage {

	UNKNOWN_SERVER_ERROR("[❎ ERROR] 서버에서 알 수 없는 에러가 발생했습니다.");

	private final String message;
}
