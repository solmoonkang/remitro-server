package com.remitro.common.infra;

public record DepositEventMessage(
	String receiverAccountNumber,

	Long amount
) {
}
