package com.remitro.account.domain.model.enums;

import lombok.Getter;

/**
 * 계좌의 종류(당좌/예금/적금) + 해당 타입에 대응하는 코드 값
 */
@Getter
public enum AccountType {

	CHECKING("110", "당좌"),
	SAVINGS("111", "예금"),
	DEPOSIT("112", "적금");

	private final String code;
	private final String description;

	AccountType(String code, String description) {
		this.code = code;
		this.description = description;
	}
}
