package com.remitro.transaction.domain.event;

import java.time.LocalDateTime;

public record AccountStatusUpdatedEvent(
	Long accountId,

	Long memberId,

	String previousStatus,

	String newStatus,

	String actorType,

	String reasonCode,

	LocalDateTime occurredAt
) {
}
