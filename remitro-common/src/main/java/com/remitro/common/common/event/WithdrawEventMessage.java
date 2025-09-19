package com.remitro.common.common.event;

public record WithdrawEventMessage(
	String senderAccountNumber,

	Long amount
) {
}
