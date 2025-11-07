package com.remitro.common.infra;

public record TransferEventMessage(
	String senderAccountNumber,

	String receiverAccountNumber,

	Long amount
) {
}
