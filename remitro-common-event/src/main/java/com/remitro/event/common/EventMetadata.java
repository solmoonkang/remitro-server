package com.remitro.event.common;

import java.time.Instant;

public record EventMetadata(
	Instant occurredAt,   // 이벤트 발생 시각 (UTC, 절대시간)

	String producer,      // 이벤트 발행 서비스명 (ex. remitro-member)

	String traceId,       // 요청 추적용 ID (Gateway에서 전달)

	EventStatus eventStatus    // PUBLISHED / CONSUMED / FAILED
) {

	public static EventMetadata now(String producer, String traceId) {
		return new EventMetadata(
			Instant.now(),
			producer,
			traceId,
			EventStatus.PUBLISHED
		);
	}
}
