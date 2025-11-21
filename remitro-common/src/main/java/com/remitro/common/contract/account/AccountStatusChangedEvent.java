package com.remitro.common.contract.account;

import java.time.LocalDateTime;

public record AccountStatusChangedEvent(
	Long accountId,

	Long memberId,

	String previousStatus,

	String newStatus,

	LocalDateTime changedAt
) {
}
