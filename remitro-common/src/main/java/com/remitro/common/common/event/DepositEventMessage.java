package com.remitro.common.common.event;

public record DepositEventMessage(
	String receiverAccountNumber,

	Long amount
) {
}
