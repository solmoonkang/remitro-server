package com.remitro.common.infra.error.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorMessage {

	// 400: BAD REQUEST
	INVALID_PASSWORD("입력하신 비밀번호는 틀린 비밀번호입니다."),
	INVALID_TOKEN("유효하지 않은 토큰입니다."),
	INSUFFICIENT_FUNDS("잔액이 충분하지 않습니다."),
	INVALID_AMOUNT("금액은 0보다 커야 합니다."),
	INVALID_TRANSFER("유효하지 않은 송금입니다."),
	INVALID_STATUS_CHANGE("이미 해지된 계좌는 상태를 변경할 수 없습니다."),
	INACTIVE_ACCOUNT_STATUS("정상 상태의 계좌가 아닙니다."),

	// 401: UNAUTHORIZED
	TOKEN_EXPIRED("이미 만료된 토큰입니다."),

	// 403: FORBIDDEN
	ACCOUNT_ACCESS_FORBIDDEN("해당 계좌에 접근할 권한이 없습니다."),
	TRANSACTION_ACCESS_FORBIDDEN("해당 거래에 접근할 권한이 없습니다."),

	// 404: NOT FOUND
	MEMBER_NOT_FOUND("요청하신 회원은 존재하지 않습니다."),
	ACCOUNT_NOT_FOUND("요청하신 계좌는 존재하지 않습니다."),
	TRANSACTION_NOT_FOUND("요청하신 거래는 존재하지 않습니다."),

	// 409: CONFLICT
	EMAIL_DUPLICATED("이미 사용중인 이메일입니다."),
	NICKNAME_DUPLICATED("이미 사용중인 닉네임입니다."),
	PHONE_NUMBER_DUPLICATED("이미 사용중인 전화번호입니다."),
	IDEMPOTENCY_KEY_DUPLICATED("이미 처리된 거래입니다."),
	LOCK_ACQUISITION_FAILED("동시성 충돌로 인해 락 획득에 실패했습니다."),
	LOCK_INTERRUPTED("분산 락 작업 중 스레드 인터럽트가 발생했습니다."),
	ACCOUNT_NUMBER_COLLISION("계좌 번호 충돌이 반복되어 계좌 생성에 실패했습니다."),
	IDEMPOTENCY_KEY_MISSING("멱등성 처리를 위해 요청 헤더에 ID가 누락되었습니다."),
	DUPLICATE_IDEMPOTENCY_REQUEST("이미 완료된 멱등성 요청으로 처리할 수 없습니다."),
	MEMBER_INACTIVE("비활성화된 사용자입니다."),

	// 500: INTERNAL SERVER ERROR
	ACCOUNT_NUMBER_GENERATION_FAILED("계좌 번호 생성에 실패했습니다."),
	UNKNOWN_SERVER_ERROR("서버에서 알 수 없는 에러가 발생했습니다.");

	private final String message;

	public String formattedMessage(Object... args) {
		return String.format(this.message, args);
	}
}
