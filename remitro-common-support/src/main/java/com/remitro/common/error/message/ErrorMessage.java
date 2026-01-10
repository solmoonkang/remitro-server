package com.remitro.common.error.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

	/* ==========================
	 * 400 BAD REQUEST
	 * ========================== */
	PASSWORD_INVALID("비밀번호가 올바르지 않습니다."),
	INVALID_REQUEST("요청 형식이 올바르지 않습니다."),

	MEMBER_STATE_INVALID("현재 회원 상태에서는 요청을 처리할 수 없습니다."),
	MEMBER_KYC_ALREADY_VERIFIED("이미 인증이 완료된 회원입니다."),
	KYC_CANNOT_START("현재 상태에서는 KYC를 시작할 수 없습니다."),
	KYC_CANNOT_VERIFY("현재 상태에서는 KYC를 승인할 수 없습니다."),
	KYC_CANNOT_REJECT("현재 상태에서는 KYC를 거절할 수 없습니다."),

	ACCOUNT_OPERATION_NOT_ALLOWED("현재 계좌 상태에서는 요청을 처리할 수 없습니다."),
	INVALID_EVENT("요청한 이벤트 정보가 올바르지 않습니다."),

	/* ==========================
	 * 401 UNAUTHORIZED
	 * ========================== */
	TOKEN_INVALID("유효하지 않은 인증 토큰입니다."),
	TOKEN_EXPIRED("만료된 인증 토큰입니다."),
	AUTHENTICATION_NOT_ALLOWED("인증을 진행할 수 없는 상태입니다."),

	MEMBER_LOCKED("계정이 잠겨 있어 로그인할 수 없습니다."),
	MEMBER_WITHDRAWN("탈퇴한 계정입니다."),
	MEMBER_DORMANT("휴면 계정입니다. 휴면 해제 후 이용해 주세요."),

	KYC_NOT_ALLOWED("인증이 완료되지 않아 요청을 처리할 수 없습니다."),

	/* ==========================
	 * 404 NOT FOUND
	 * ========================== */
	MEMBER_ID_NOT_FOUND("회원 ID %s를 찾을 수 없습니다."),
	MEMBER_EMAIL_NOT_FOUND("회원 EMAIL %s를 찾을 수 없습니다."),
	MEMBER_NICKNAME_NOT_FOUND("닉네임 %s를 가진 회원을 찾을 수 없습니다."),

	ACCOUNT_NOT_FOUND("요청하신 계좌를 찾을 수 없습니다."),
	TRANSACTION_NOT_FOUND("요청하신 거래를 찾을 수 없습니다."),

	/* ==========================
	 * 409 CONFLICT
	 * ========================== */
	DUPLICATE_EMAIL("이미 사용 중인 이메일입니다."),
	DUPLICATE_NICKNAME("이미 사용 중인 닉네임입니다."),
	DUPLICATE_PHONE_NUMBER("이미 사용 중인 전화번호입니다."),

	DUPLICATE_REQUEST("이미 처리된 요청입니다."),
	INSUFFICIENT_FUNDS("잔액이 부족합니다."),
	ACCOUNT_LIMIT_EXCEEDED("계좌 개설 한도를 초과했습니다."),

	/* ==========================
	 * 500 INTERNAL SERVER ERROR / 503 SERVICE UNAVAILABLE
	 * ========================== */
	INTERNAL_SERVER_ERROR("처리 중 오류가 발생했습니다."),
	TEMPORARY_UNAVAILABLE("요청이 많아 잠시 후 다시 시도해 주세요.");

	private final String message;

	public String formattedMessage(Object... args) {
		return String.format(this.message, args);
	}
}
