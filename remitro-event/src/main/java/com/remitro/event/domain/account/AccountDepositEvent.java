package com.remitro.event.domain.account;

import java.time.LocalDateTime;

public record AccountDepositEvent(
	Long accountId,

	Long memberId,

	Long amount,

	Long balanceAfter,

	String description,

	LocalDateTime occurredAt
) {
}
