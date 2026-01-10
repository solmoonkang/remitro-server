package com.remitro.account.domain.account.enums;

import lombok.Getter;

@Getter
public enum Category {

	DEPOSIT("입출금 계좌"),
	LOAN("대출 계좌"),
	VIRTUAL("가상 계좌");

	private final String defaultAccountName;

	Category(String defaultAccountName) {
		this.defaultAccountName = defaultAccountName;
	}
}
