package com.remitro.account.domain.event.account;

import java.time.LocalDateTime;

public record AccountStatusUpdateRequestedEvent(
	Long accountId,

	String targetStatus,

	String reasonCode,

	LocalDateTime occurredAt,

	int schemaVersion
) {
}
