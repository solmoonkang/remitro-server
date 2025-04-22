package com.remitroserver.api.application.transaction.mapper;

import com.remitroserver.api.domain.transaction.entity.Transaction;
import com.remitroserver.api.dto.transaction.response.TransactionSummaryResponse;

public class TransactionMapper {

	public static TransactionSummaryResponse toSummaryResponse(Transaction transaction) {
		return TransactionSummaryResponse.builder()
			.transactionToken(transaction.getTransactionToken())
			.fromAccountNumber(transaction.getFromAccount().getAccountNumber())
			.toAccountNumber(transaction.getToAccount().getAccountNumber())
			.amount(transaction.getAmount().getValue())
			.createdAt(transaction.getCreatedAt())
			.build();
	}
}
