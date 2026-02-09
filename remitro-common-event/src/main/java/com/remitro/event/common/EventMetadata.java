package com.remitro.event.common;

import java.time.LocalDateTime;

public record EventMetadata(
	String eventId,

	LocalDateTime occurredAt,

	String schemaVersion,

	String traceId,

	String correlationId,

	String producer
) {
}
