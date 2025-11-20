package com.remitro.common.contract.account;

import java.time.LocalDateTime;

public record AccountDepositEvent(
	Long accountId,

	Long memberId,

	Long amount,

	Long balanceAfter,

	String description,

	LocalDateTime occurredAt
) implements AccountTransactionEvent {
}
