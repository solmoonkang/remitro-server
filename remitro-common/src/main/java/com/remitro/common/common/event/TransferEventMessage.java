package com.remitro.common.common.event;

public record TransferEventMessage(
	String senderAccountNumber,

	String receiverAccountNumber,

	Long amount
) {
}
