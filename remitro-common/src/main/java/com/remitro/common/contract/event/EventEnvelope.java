package com.remitro.common.contract.event;

import java.time.LocalDateTime;

public record EventEnvelope(
	String eventId,

	String eventType,

	String aggregateType,

	Long aggregateId,

	LocalDateTime occurredAt,

	String eventPayload
) {
}
