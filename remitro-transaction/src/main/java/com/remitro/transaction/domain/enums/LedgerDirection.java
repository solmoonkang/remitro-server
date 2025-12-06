package com.remitro.transaction.domain.enums;

/**
 * 원장 라인이 계좌 잔액에 미치는 영향
 * DEBIT -> 잔액 감소
 * CREDIT -> 잔액 증가
 */
public enum LedgerDirection {

	DEBIT,
	CREDIT
}
