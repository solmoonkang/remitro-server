package com.remitro.transaction.domain.event;

import java.time.LocalDateTime;

import com.remitro.transaction.domain.enums.TransactionType;

public interface AccountTransactionEvent {
	Long accountId();

	Long amount();

	TransactionType transactionType();

	String description();

	LocalDateTime occurredAt();
}
