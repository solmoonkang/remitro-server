package com.remitro.common.domain.enums;

/**
 * Outbox 이벤트의 발행 상태
 * PENDING -> DB에는 들어갔지만 아직 Kafka 등으로 PUBLISH 안 된 상태
 * PUBLISHED -> 외부로 전파 완료된 상태
 */
public enum EventStatus {

	PENDING,
	PUBLISHED,
	FAILED
}

