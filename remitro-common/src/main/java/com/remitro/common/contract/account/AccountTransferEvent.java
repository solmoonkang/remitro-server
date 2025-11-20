package com.remitro.common.contract.account;

import java.time.LocalDateTime;

public record AccountTransferEvent(
	Long fromAccountId,

	Long toAccountId,

	Long amount,

	Long fromBalanceAfter,

	Long toBalanceAfter,

	String description,

	LocalDateTime occurredAt
) {
}
