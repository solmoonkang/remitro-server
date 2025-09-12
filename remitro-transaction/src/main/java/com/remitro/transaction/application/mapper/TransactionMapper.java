package com.remitro.transaction.application.mapper;

import java.util.List;

import com.remitro.transaction.application.dto.response.TransactionDetailResponse;
import com.remitro.transaction.domain.model.Transaction;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransactionMapper {

	public static TransactionDetailResponse toTransactionDetailResponse(Transaction transaction) {
		return new TransactionDetailResponse(transaction.getSenderAccount().getAccountNumber(),
			transaction.getReceiverAccount().getAccountNumber(), transaction.getTransactionType().name(),
			transaction.getAmount(), transaction.getBalanceSnapshot(), transaction.getTransactionAt());
	}

	public static List<TransactionDetailResponse> toTransactionListResponse(List<Transaction> transactions) {
		return transactions.stream()
			.map(TransactionMapper::toTransactionDetailResponse)
			.toList();
	}
}
