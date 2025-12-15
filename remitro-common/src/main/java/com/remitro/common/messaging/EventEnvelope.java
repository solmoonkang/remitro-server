package com.remitro.common.messaging;

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
