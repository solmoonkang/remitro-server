package com.remitro.transaction.infrastructure.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountEventType {

	public static final String ACCOUNT_OPENED = "ACCOUNT_OPENED";
	public static final String ACCOUNT_STATUS_UPDATED = "ACCOUNT_STATUS_UPDATED";
	public static final String ACCOUNT_DEPOSIT = "ACCOUNT_DEPOSIT";
	public static final String ACCOUNT_WITHDRAWAL = "ACCOUNT_WITHDRAWAL";
	public static final String ACCOUNT_TRANSFER = "ACCOUNT_TRANSFER";
}
