package com.remitro.account.domain.event.account;

import java.time.LocalDateTime;

public record AccountStatusUpdatedEvent(
	Long accountId,

	Long memberId,

	String previousStatus,

	String currentStatus,

	String actorType,

	String reasonCode,

	LocalDateTime occurredAt,

	int schemaVersion
) {
}
