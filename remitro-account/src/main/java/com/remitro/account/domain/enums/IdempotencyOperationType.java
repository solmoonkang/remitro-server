package com.remitro.account.domain.enums;

public enum IdempotencyOperationType {

	OPEN_ACCOUNT,
	UPDATE_ACCOUNT_STATUS,

	DEPOSIT,
	WITHDRAW,
	TRANSFER
}
