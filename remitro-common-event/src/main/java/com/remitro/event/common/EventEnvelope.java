package com.remitro.event.common;

public record EventEnvelope<T>(
	EventMetadata eventMetadata,

	EventType eventType,

	String partitionKey,

	T payload
) {
}
