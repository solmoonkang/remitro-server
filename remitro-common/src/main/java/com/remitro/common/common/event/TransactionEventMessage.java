package com.remitro.common.common.event;

public record TransactionEventMessage(
	String senderAccountNumber,

	String receiverAccountNumber,

	Long amount,

	String transactionType
) {
}
