package com.remitro.account.application.dto.request.deposit;

public record DepositCommand(
	Long memberId,

	Long accountId,

	Long amount,

	String idempotencyKey
) {
}
