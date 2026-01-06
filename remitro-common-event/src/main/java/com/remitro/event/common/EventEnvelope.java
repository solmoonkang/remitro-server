package com.remitro.event.common;

import java.util.UUID;

public record EventEnvelope<T>(
	String eventId,              // 이벤트 고유 식별자 (UUID)

	AggregateType aggregateType, // MEMBER, ACCOUNT, ...

	String aggregateId,          // Aggregate 식별자 (문자열로 고정)

	EventType eventType,         // CREATED, STATUS_CHANGED, ...

	EventVersion eventVersion,        // V1, V2 ...

	EventMetadata eventMetadata,      // 공통 메타데이터

	T payload                    // 도메인 이벤트 DTO
) {

	public static <T> EventEnvelope<T> of(
		AggregateType aggregateType,
		EventType eventType,
		String aggregateId,
		String producer,
		String traceId,
		T payload
	) {
		return new EventEnvelope<>(
			UUID.randomUUID().toString(),
			aggregateType,
			aggregateId,
			eventType,
			EventVersion.V1,
			EventMetadata.now(producer, traceId),
			payload
		);
	}
}
