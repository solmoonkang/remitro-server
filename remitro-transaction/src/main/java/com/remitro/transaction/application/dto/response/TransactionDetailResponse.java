package com.remitro.transaction.application.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record TransactionDetailResponse(
	String senderAccountNumber,

	String receiverAccountNumber,

	String transactionType,

	Long amount,

	Long balanceSnapshot,

	LocalDateTime transactionAt
) {
}
