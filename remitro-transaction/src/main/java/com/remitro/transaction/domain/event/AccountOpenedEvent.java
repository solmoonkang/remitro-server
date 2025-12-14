package com.remitro.transaction.domain.event;

import java.time.LocalDateTime;

public record AccountOpenedEvent(
	Long accountId,

	Long memberId,

	String initialStatus,

	String actorType,

	String reasonCode,

	LocalDateTime occurredAt,

	int schemaVersion
) {
}
