package com.remitro.transaction.domain.enums;

public enum AmlRuleCode {

	HIGH_AMOUNT_SINGLE,                    // 1회 거래 금액이 비정상적으로 큼
	RAPID_IN_OUT,                          // 짧은 시간 내 입금 -> 전액 출금
	NEW_ACCOUNT_LARGE_TRANSFER             // 신규 계좌에서 대량 자금 이동
}
