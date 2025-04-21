package com.remitroserver.api.domain.account.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountType {

	CHECKING("01", 1), SAVING("02", 5), CMA("03", 1);

	private final String code;
	private final int maxAccount;
}
