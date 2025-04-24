package com.remitroserver.global.error.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorMessage {

	INVALID_MONEY_VALUE_ERROR("[❎ ERROR] 금액은 0원 이상이어야 합니다."),
	INSUFFICIENT_BALANCE_ERROR("[❎ ERROR] 잔액이 부족합니다."),
	ACCOUNT_NOT_SUSPENDED_ERROR("[❎ ERROR] 정지된 계좌만 복구할 수 있습니다."),
	ACCOUNT_INVALID_STATUS_TRANSITION_ERROR("[❎ ERROR] 허용되지 않은 계좌 상태 전이입니다."),
	ACCOUNT_BALANCE_NOT_ZERO_ERROR("[❎ ERROR] 잔액이 0원이어야 계좌를 해지할 수 있습니다."),
	ACCOUNT_NOT_ACTIVE_ERROR("[❎ ERROR] 계좌가 활성 상태가 아닙니다."),
	ACCOUNT_ALREADY_CLOSED_ERROR("[❎ ERROR] 해지된 계좌는 정지할 수 없습니다."),
	PASSWORD_MISMATCH_ERROR("[❎ ERROR] 입력하신 비밀번호가 서로 일치하지 않습니다."),
	INVALID_REGISTRATION_NUMBER_FORMAT_ERROR("[❎ ERROR] 올바르지 않은 주민등록번호 형식입니다."),
	TRANSFER_TO_SAME_ACCOUNT_ERROR("[❎ ERROR] 동일한 계좌로는 송금할 수 없습니다."),
	INVALID_TRANSACTION_STATUS_FOR_CANCEL_ERROR("[❎ ERROR] 취소 가능한 상태가 아닙니다."),
	TRANSACTION_NOT_REQUESTED_ERROR("[❎ ERROR] 요청 상태의 거래만 취소할 수 있습니다."),
	TRANSACTION_EXPIRED_ERROR("[❎ ERROR] 거래 승인 가능 시간을 초과했습니다."),

	UNAUTHORIZED_REQUEST_ERROR("[❎ ERROR] 인증되지 않은 사용자 요청입니다."),
	INVALID_AUTHORIZATION_HEADER_ERROR("[❎ ERROR] 유효하지 않은 AUTHORIZATION 헤더입니다."),
	UNAUTHORIZED_REFRESH_TOKEN_ERROR("[❎ ERROR] 유효하지 않은 리프레시 토큰입니다."),

	ACCOUNT_ACCESS_DENIED_ERROR("[❎ ERROR] 해당 계좌에 접근할 권한이 없습니다."),
	TRANSACTION_ACCESS_DENIED_ERROR("[❎ ERROR] 해당 계좌에 접근할 권한이 없습니다."),

	MEMBER_NOT_FOUND_ERROR("[❎ ERROR] 요청하신 회원을 찾을 수 없습니다."),
	ACCOUNT_NOT_FOUND_ERROR("[❎ ERROR] 요청하신 계좌를 찾을 수 없습니다."),
	TRANSACTION_NOT_FOUND_ERROR("[❎ ERROR] 요청 상태의 거래를 찾을 수 없습니다."),

	DUPLICATED_EMAIL_ERROR("[❎ ERROR] 이미 존재하는 사용자 이메일입니다."),
	DUPLICATED_NICKNAME_ERROR("[❎ ERROR] 이미 존재하는 사용자 닉네임입니다."),
	DUPLICATED_REGISTRATION_NUMBER_ERROR("[❎ ERROR] 이미 가입된 사용자입니다."),
	DUPLICATED_IDEMPOTENCY_KEY_ERROR("[❎ ERROR] 이미 처리된 송금 요청입니다."),
	ACCOUNT_NUMBER_GENERATION_FAILED_ERROR("[❎ ERROR] 이미 존재하는 계좌 번호입니다."),
	ACCOUNT_TYPE_LIMIT_EXCEEDED_ERROR("[❎ ERROR] 해당 계좌 유형은 최대 생성 개수를 초과했습니다."),
	ACCOUNT_CONCURRENCY_ERROR("[❎ ERROR] 잔액 변경 충돌이 발생했습니다. 잠시 후 다시 시도해주세요."),

	UNKNOWN_SERVER_ERROR("[❎ ERROR] 서버에서 알 수 없는 에러가 발생했습니다."),
	UTILITY_CLASS_INSTANTIATION_ERROR("[❎ ERROR] 유틸리티 클래스는 인스턴스화할 수 없습니다."),
	AES_INITIALIZE_ERROR("[❎ ERROR] AES 초기화 중 예외가 발생했습니다."),
	AES_ENCRYPT_ERROR("[❎ ERROR] AES 암호화 중 오류가 발생했습니다."),
	AES_DECRYPT_ERROR("[❎ ERROR] AES 복호화 중 오류가 발생했습니다.");

	private final String message;
}
