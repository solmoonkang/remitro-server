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

	UNAUTHORIZED_REQUEST_ERROR("[❎ ERROR] 인증되지 않은 사용자 요청입니다."),
	INVALID_AUTHORIZATION_HEADER_ERROR("[❎ ERROR] 유효하지 않은 AUTHORIZATION 헤더입니다."),
	UNAUTHORIZED_REFRESH_TOKEN_ERROR("[❎ ERROR] 유효하지 않은 리프레시 토큰입니다."),

	DUPLICATED_EMAIL_ERROR("[❎ ERROR] 이미 존재하는 사용자 이메일입니다."),
	DUPLICATED_NICKNAME_ERROR("[❎ ERROR] 이미 존재하는 사용자 닉네임입니다."),
	DUPLICATED_REGISTRATION_NUMBER_ERROR("[❎ ERROR] 이미 가입된 사용자입니다."),
	PASSWORD_MISMATCH_ERROR("[❎ ERROR] 입력하신 비밀번호가 서로 일치하지 않습니다."),
	INVALID_REGISTRATION_NUMBER_FORMAT_ERROR("[❎ ERROR] 올바르지 않은 주민등록번호 형식입니다."),
	MEMBER_NOT_FOUND_ERROR("[❎ ERROR] 요청하신 회원을 찾을 수 없습니다.");

	private final String message;
}
