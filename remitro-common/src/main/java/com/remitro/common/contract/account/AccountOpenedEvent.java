package com.remitro.common.contract.account;

public record AccountOpenedEvent(
	Long accountId,

	Long memberId,

	String accountTypeCode
) {
}
