package com.remitroserver.global.error.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorMessage {

	UNKNOWN_SERVER_ERROR("[❎ ERROR] 서버에서 알 수 없는 에러가 발생했습니다."),

	AES_INITIALIZE_ERROR("[❎ ERROR] AES 초기화 중 예외가 발생했습니다."),
	AES_ENCRYPT_ERROR("[❎ ERROR] AES 암호화 중 오류가 발생했습니다."),
	AES_DECRYPT_ERROR("[❎ ERROR] AES 복호화 중 오류가 발생했습니다."),

	DUPLICATED_EMAIL("[❎ ERROR] 이미 존재하는 사용자 이메일입니다."),
	DUPLICATED_NICKNAME("[❎ ERROR] 이미 존재하는 사용자 닉네임입니다."),
	DUPLICATED_REGISTRATION_NUMBER("[❎ ERROR] 이미 가입된 사용자입니다."),
	PASSWORD_MISMATCH("[❎ ERROR] 입력하신 비밀번호가 서로 일치하지 않습니다.");

	private final String message;
}
