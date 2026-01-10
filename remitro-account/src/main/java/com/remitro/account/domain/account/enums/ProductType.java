package com.remitro.account.domain.account.enums;

import lombok.Getter;

@Getter
public enum ProductType {

	CHECKING("110", Category.DEPOSIT),
	SAVINGS("111", Category.DEPOSIT),
	FIXED_DEPOSIT("112", Category.DEPOSIT),
	INSTALLMENT_SAVINGS("113", Category.DEPOSIT),
	CMA("114", Category.DEPOSIT),

	LOAN("210", Category.LOAN),

	VIRTUAL("900", Category.VIRTUAL);

	private final String code;
	private final Category category;

	ProductType(String code, Category category) {
		this.code = code;
		this.category = category;
	}
}
