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

	AMOUNT_INVALID("금액은 0보다 커야 합니다."),
	ACCOUNT_OPERATION_NOT_ALLOWED("현재 계좌 상태에서는 요청을 처리할 수 없습니다."),

	TRANSFER_INVALID("유효하지 않은 거래 요청입니다."),

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
	 * 403 FORBIDDEN
	 * ========================== */

	ACCESS_FORBIDDEN("해당 요청을 수행할 권한이 없습니다."),
	ADMIN_PERMISSION_REQUIRED("관리자 권한이 필요합니다."),

	/* ==========================
	 * 404 NOT FOUND
	 * ========================== */

	MEMBER_NOT_FOUND("요청하신 회원을 찾을 수 없습니다."),
	ACCOUNT_NOT_FOUND("요청하신 계좌를 찾을 수 없습니다."),
	TRANSACTION_NOT_FOUND("요청하신 거래를 찾을 수 없습니다."),
	RESOURCE_NOT_FOUND("요청하신 자원을 찾을 수 없습니다."),

	/* ==========================
	 * 409 CONFLICT
	 * ========================== */

	DUPLICATE_RESOURCE("이미 존재하는 자원입니다."),
	DUPLICATE_REQUEST("이미 처리된 요청입니다."),
	INSUFFICIENT_FUNDS("잔액이 부족합니다."),

	/* ==========================
	 * 500 INTERNAL SERVER ERROR
	 * ========================== */

	INTERNAL_SERVER_ERROR("처리 중 오류가 발생했습니다."),

	/* ==========================
	 * 503 SERVICE UNAVAILABLE
	 * ========================== */

	TEMPORARY_UNAVAILABLE("요청이 많아 잠시 후 다시 시도해 주세요.");

	private final String message;

	public String formattedMessage(Object... args) {
		return String.format(this.message, args);
	}
}
