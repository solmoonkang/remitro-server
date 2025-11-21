package com.remitro.account.domain.model.enums;

import lombok.Getter;

/**
 * 계좌의 종류(당좌/예금/적금) + 해당 타입에 대응하는 코드 값
 */
@Getter
public enum AccountType {

	CHECKING("110", "당좌"),
	SAVINGS("111", "예금"),
	FIXED_DEPOSIT("112", "정기예금"),
	INSTALLMENT_SAVINGS("113", "적립식 적금"),
	CMA("114", "CMA 계좌"),

	LOAN("210", "대출계좌"),
	CREDIT_LIMIT("220", "한도대출 계좌"),

	FOREIGN_CURRENCY("310", "외화예금"),
	HIGH_INTEREST_SAVING("320", "고금리 예적금"),

	VIRTUAL("900", "가상 계좌");

	private final String code;
	private final String description;

	AccountType(String code, String description) {
		this.code = code;
		this.description = description;
	}
}
