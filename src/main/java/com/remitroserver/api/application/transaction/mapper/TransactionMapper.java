package com.remitroserver.api.application.transaction.mapper;

import java.util.List;

import com.remitroserver.api.domain.transaction.entity.Transaction;
import com.remitroserver.api.domain.transaction.entity.TransactionStatusLog;
import com.remitroserver.api.dto.transaction.response.TransactionDetailResponse;
import com.remitroserver.api.dto.transaction.response.TransactionStatusLogResponse;
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

	public static TransactionDetailResponse toDetailResponse(
		Transaction transaction,
		List<TransactionStatusLog> transactionStatusLogs) {

		return TransactionDetailResponse.builder()
			.transactionToken(transaction.getTransactionToken())
			.fromAccountNumber(transaction.getFromAccount().getAccountNumber())
			.fromNickname(transaction.getFromAccount().getMember().getNickname())
			.toAccountNumber(transaction.getToAccount().getAccountNumber())
			.toNickname(transaction.getToAccount().getMember().getNickname())
			.amount(transaction.getAmount().getValue())
			.createdAt(transaction.getCreatedAt())
			.statusLogs(toLogResponse(transactionStatusLogs))
			.build();
	}

	public static TransactionStatusLogResponse toLogResponse(TransactionStatusLog transactionStatusLog) {
		return TransactionStatusLogResponse.builder()
			.status(transactionStatusLog.getStatus())
			.changedAt(transactionStatusLog.getCreatedAt())
			.build();
	}

	private static List<TransactionStatusLogResponse> toLogResponse(List<TransactionStatusLog> transactionStatusLogs) {
		return transactionStatusLogs.stream()
			.map(TransactionMapper::toLogResponse)
			.toList();
	}
}
