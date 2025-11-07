package com.remitro.common.infra;

public record TransactionEventMessage(
	String senderAccountNumber,

	String receiverAccountNumber,

	Long amount,

	String transactionType,

	String idempotencyKey
) {
}
