package com.remitroserver.api.application.transaction.mapper;

import java.util.List;

import com.remitroserver.api.domain.transaction.entity.Transaction;
import com.remitroserver.api.domain.transaction.entity.StatusLog;
import com.remitroserver.api.dto.transaction.request.TransferRequest;
import com.remitroserver.api.dto.transaction.response.TransactionDetailResponse;
import com.remitroserver.api.dto.transaction.response.TransactionStatusLogResponse;
import com.remitroserver.api.dto.transaction.response.TransactionSummaryResponse;

public class TransactionMapper {

	public static TransferRequest toTransferRequest(Transaction transaction) {
		return TransferRequest.builder()
			.fromAccountToken(transaction.getFromAccount().getAccountToken())
			.toAccountNumber(transaction.getToAccount().getAccountNumber())
			.amount(transaction.getAmount().getValue())
			.build();
	}

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
		List<StatusLog> statusLogs) {

		return TransactionDetailResponse.builder()
			.transactionToken(transaction.getTransactionToken())
			.fromAccountNumber(transaction.getFromAccount().getAccountNumber())
			.fromNickname(transaction.getFromAccount().getMember().getNickname())
			.toAccountNumber(transaction.getToAccount().getAccountNumber())
			.toNickname(transaction.getToAccount().getMember().getNickname())
			.amount(transaction.getAmount().getValue())
			.createdAt(transaction.getCreatedAt())
			.statusLogs(toLogResponse(statusLogs))
			.build();
	}

	public static TransactionStatusLogResponse toLogResponse(StatusLog statusLog) {
		return TransactionStatusLogResponse.builder()
			.status(statusLog.getStatus())
			.changedAt(statusLog.getCreatedAt())
			.build();
	}

	private static List<TransactionStatusLogResponse> toLogResponse(List<StatusLog> statusLogs) {
		return statusLogs.stream()
			.map(TransactionMapper::toLogResponse)
			.toList();
	}
}
