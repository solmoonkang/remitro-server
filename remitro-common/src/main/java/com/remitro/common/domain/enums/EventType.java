package com.remitro.common.domain.enums;

/**
 * Outbox에서 발행되는 "이벤트 종류"를 식별하는 값
 * 계좌 개설, 입금, 출금, 송금, 회원 Activity 변경 등을 구분
 */
public enum EventType {

	ACCOUNT_OPENED,
	DEPOSIT,
	WITHDRAWAL,
	TRANSFER,
	ACCOUNT_STATUS_CHANGED,

	MEMBER_ACTIVITY_UPSERTED
}

