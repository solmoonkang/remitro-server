package com.remitro.common.infra;

public record WithdrawEventMessage(
	String senderAccountNumber,

	Long amount
) {
}
