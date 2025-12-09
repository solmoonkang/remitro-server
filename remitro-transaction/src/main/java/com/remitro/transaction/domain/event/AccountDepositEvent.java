package com.remitro.transaction.domain.event;

import java.time.LocalDateTime;

import com.remitro.transaction.domain.enums.TransactionType;

public record AccountDepositEvent(
	Long accountId,

	Long memberId,

	Long amount,

	Long balanceAfter,

	String description,

	LocalDateTime occurredAt

) implements AccountTransactionEvent {

	@Override
	public TransactionType transactionType() {
		return TransactionType.DEPOSIT;
	}
}
