package com.remitro.account.domain.model.enums;

import lombok.Getter;

@Getter
public enum AccountType {

	CHECKING("110"),	// 당좌 계좌
	SAVINGS("111"),		// 예금 계좌
	DEPOSIT("112");		// 적금 계좌

	private final String code;

	AccountType(String code) {
		this.code = code;
	}
}
