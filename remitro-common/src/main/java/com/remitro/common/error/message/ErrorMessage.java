package com.remitro.common.error.message;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorMessage {

	/* ==========================
	 * 400 BAD REQUEST
	 * ========================== */

	/* MEMBER */
	PASSWORD_INVALID("비밀번호가 올바르지 않습니다."),
	MEMBER_STATE_INVALID("현재 회원 상태에서는 요청을 처리할 수 없습니다."),
	MEMBER_ALREADY_WITHDRAWN("탈퇴한 회원은 상태를 변경할 수 없습니다."),
	ACTIVITY_STATUS_TRANSITION_INVALID("LOCKED 상태에서는 직접 ACTIVE로 변경할 수 없습니다."),
	MEMBER_KYC_ALREADY_VERIFIED("이미 KYC 인증이 완료된 회원입니다."),

	/* KYC */
	KYC_ALREADY_COMPLETED("이미 처리 완료된 KYC 요청입니다."),
	KYC_APPROVAL_NOT_ALLOWED("현재 상태에서는 KYC 인증을 승인할 수 없습니다."),
	KYC_REJECTION_NOT_ALLOWED("현재 상태에서는 KYC 인증을 거절할 수 없습니다."),
	KYC_REJECTION_REASON_REQUIRED("KYC 인증 거절 시 사유는 필수입니다."),

	KYC_ALREADY_IN_PROGRESS("이미 진행 중인 KYC 요청이 존재합니다."),
	KYC_REQUEST_NOT_ALLOWED("현재 KYC 상태에서는 새로운 KYC 요청이 불가능합니다."),
	KYC_RETRY_NOT_YET_ALLOWED("이전 KYC 거절 이후 일정 기간이 지나야 다시 요청할 수 있습니다."),

	/* ROLE */
	ROLE_ALREADY_ASSIGNED("이미 해당 권한이 부여된 회원입니다."),

	/* ACCOUNT */
	AMOUNT_INVALID("금액은 0보다 커야 합니다."),
	NEGATIVE_BALANCE_NOT_ALLOWED("잔액은 음수가 될 수 없습니다."),
	ACCOUNT_STATUS_CHANGE_INVALID("현재 상태에서는 계좌 상태를 변경할 수 없습니다."),
	ACCOUNT_DEPOSIT_NOT_ALLOWED("해당 계좌 상태에서는 입금이 허용되지 않습니다."),
	ACCOUNT_WITHDRAW_NOT_ALLOWED("해당 계좌 상태에서는 출금이 허용되지 않습니다."),
	ACCOUNT_TRANSFER_NOT_ALLOWED("해당 계좌 상태에서는 송금이 허용되지 않습니다."),
	ACCOUNT_ALREADY_TERMINATED("이미 해지된 계좌입니다."),
	ACCOUNT_STATUS_TRANSITION_NOT_ALLOWED("요청하신 계좌 상태로 변경할 수 없습니다."),

	/* TRANSACTION */
	TRANSFER_INVALID("유효하지 않은 송금 요청입니다."),
	INSUFFICIENT_FUNDS("잔액이 부족합니다."),

	/* EVENT */
	EVENT_HEADER_MISSING("필수 이벤트 헤더가 누락되었습니다."),
	EVENT_PAYLOAD_INVALID("이벤트 데이터가 올바르지 않습니다."),

	/* IDEMPOTENCY */
	IDEMPOTENCY_KEY_MISSING("멱등성 처리를 위한 Idempotency-Key가 필요합니다."),
	IDEMPOTENCY_KEY_DUPLICATED("이 요청은 이미 처리되었습니다."),

	/* ==========================
	 * 401 UNAUTHORIZED
	 * ========================== */

	TOKEN_EXPIRED("만료된 인증 토큰입니다."),
	TOKEN_INVALID("유효하지 않은 인증 토큰입니다."),

	MEMBER_LOCKED_NOT_ALLOWED("잠금 처리된 회원은 로그인할 수 없습니다."),
	MEMBER_WITHDRAWN_NOT_ALLOWED("탈퇴한 회원은 로그인할 수 없습니다."),
	MEMBER_DORMANT_NOT_ALLOWED("휴면 상태의 회원입니다. 휴면 해제 후 이용해 주세요."),

	KYC_NOT_ALLOWED_FOR_LOGIN("KYC 인증이 완료되지 않아 로그인할 수 없습니다."),
	KYC_NOT_ALLOWED_FOR_REISSUE("KYC 인증이 완료되지 않아 토큰을 재발급할 수 없습니다."),
	KYC_REJECTED("KYC 인증이 반려된 회원입니다."),

	AUTHENTICATION_NOT_ALLOWED("현재 상태에서는 인증을 진행할 수 없습니다."),

	/* ==========================
	 * 403 FORBIDDEN
	 * ========================== */

	ACCOUNT_ACCESS_FORBIDDEN("해당 계좌에 접근할 권한이 없습니다."),
	TRANSACTION_ACCESS_FORBIDDEN("해당 거래에 접근할 권한이 없습니다."),
	MEMBER_ACCESS_FORBIDDEN("해당 회원 정보에 접근할 권한이 없습니다."),
	ADMIN_PERMISSION_REQUIRED("관리자 권한이 필요합니다."),

	/* ==========================
	 * 404 NOT FOUND
	 * ========================== */

	MEMBER_NOT_FOUND("요청하신 회원을 찾을 수 없습니다."),
	ACCOUNT_NOT_FOUND("요청하신 계좌를 찾을 수 없습니다."),
	KYC_VERIFICATION_NOT_FOUND("요청하신 KYC 인증 정보를 찾을 수 없습니다."),
	TRANSACTION_NOT_FOUND("요청하신 거래 내역을 찾을 수 없습니다."),
	EVENT_NOT_FOUND("요청하신 이벤트 내역을 찾을 수 없습니다."),

	/* ==========================
	 * 409 CONFLICT
	 * ========================== */

	/* MEMBER */
	EMAIL_ALREADY_EXISTS("이미 사용 중인 이메일입니다."),
	NICKNAME_ALREADY_EXISTS("이미 사용 중인 닉네임입니다."),
	PHONE_NUMBER_ALREADY_EXISTS("이미 사용 중인 전화번호입니다."),

	/* ACCOUNT */
	ACCOUNT_NUMBER_COLLISION("계좌 번호 충돌로 계좌 생성에 실패했습니다."),
	ACCOUNT_TYPE_LIMIT_EXCEEDED("해당 종류의 계좌 개설 가능 개수를 초과했습니다."),
	ACCOUNT_LIMIT_EXCEEDED("개설 가능한 전체 계좌 수를 초과했습니다."),
	DORMANT_ACCOUNT_STATUS_CHANGE_NOT_ALLOWED("휴면 계좌는 상태를 변경할 수 없습니다."),
	ACCOUNT_STATUS_ALREADY_SAME("이미 동일한 계좌 상태입니다."),
	ACCOUNT_SUSPENDED_RESTORE_NOT_ALLOWED("이용 정지 계좌는 정상 상태로 복구할 수 없습니다."),
	DORMANT_ACCOUNT_RESTORE_NOT_ALLOWED("휴면 계좌는 정상 상태로 전환할 수 없습니다."),
	ACCOUNT_FROZEN_DORMANT_NOT_ALLOWED("지급 정지 계좌는 휴면 상태로 전환할 수 없습니다."),

	/* IDEMPOTENCY */
	DUPLICATE_IDEMPOTENCY_REQUEST("이미 완료된 요청입니다."),

	/* LOCK */
	LOCK_ACQUISITION_FAILED("락 획득 중 충돌이 발생했습니다."),
	LOCK_INTERRUPTED("락 처리 중 스레드 인터럽트가 발생했습니다."),

	/* TRANSACTION */
	ACCOUNT_STATUS_HISTORY_PREVIOUS_REQUIRED("계좌 상태 변경 이력에는 이전 상태가 반드시 존재해야 합니다."),
	ACCOUNT_STATUS_HISTORY_DUPLICATED("이전 상태와 동일한 상태로의 변경 이력은 허용되지 않습니다."),
	ACCOUNT_STATUS_HISTORY_PREVIOUS_MUST_BE_NULL("계좌 개설 이력에는 이전 상태가 존재할 수 없습니다."),

	/* SUSPICIOUS */
	SUSPICIOUS_TRANSACTION_ONLY_OPEN_REVIEWABLE("의심 거래는 OPEN 상태에서만 REVIEWED로 변경할 수 있습니다."),
	SUSPICIOUS_TRANSACTION_ALREADY_CLOSED("이미 CLOSED 상태인 의심 거래입니다."),

	MEMBER_NOT_ELIGIBLE("해당 회원은 현재 요청을 처리할 수 있는 상태가 아닙니다."),
	UNSUPPORTED_EVENT_SCHEMA("지원하지 않는 이벤트 스키마 값입니다."),

	/* ==========================
	 * 500 INTERNAL SERVER ERROR
	 * ========================== */

	ACCOUNT_NUMBER_GENERATION_FAILED("계좌 번호 생성 중 오류가 발생했습니다."),
	BALANCE_OVERFLOW("잔액이 허용 가능한 범위를 초과했습니다."),
	ACCOUNT_BALANCE_CORRUPTED("계좌 잔액 무결성 오류가 발생했습니다: %s"),
	UNKNOWN_SERVER_ERROR("서버 내부 오류가 발생했습니다."),

	/* ==========================
	 * 503 SERVICE UNAVAILABLE
	 * ========================== */

	UNABLE_TO_ACQUIRE_LOCK("현재 요청량이 많아 잠시 후 다시 시도해 주세요.");

	private final String message;

	public String formattedMessage(Object... args) {
		return String.format(this.message, args);
	}
}
