package com.remitro.transaction.domain.enums;

/**
 * AML 룰 엔진에서 "어떤 규칙에 걸렸는지"를 나타내는 코드 값
 * HIGH_AMOUNT_SINGLE -> 단일 고액 거래
 * RAPID_IN_OUT -> 단기간 입출금 반복
 * NEW_ACCOUNT_LARGE_TRANSFER -> 신규 계좌의 고액 송금
 */
public enum AmlRuleCode {

	HIGH_AMOUNT_SINGLE,
	RAPID_IN_OUT,
	NEW_ACCOUNT_LARGE_TRANSFER
}
