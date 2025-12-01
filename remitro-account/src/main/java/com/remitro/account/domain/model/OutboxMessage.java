package com.remitro.account.domain.model;

import java.util.UUID;

import com.remitro.account.domain.enums.AggregateType;
import com.remitro.account.domain.event.EventStatus;
import com.remitro.account.domain.event.EventType;
import com.remitro.account.infrastructure.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "ACCOUNT_OUTBOX_MESSAGES", indexes = {
	@Index(name = "idx_outbox_status_created_at", columnList = "event_status, created_at"),
	@Index(name = "idx_outbox_event_id", columnList = "event_id", unique = true)
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxMessage extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "outbox_message_id", nullable = false)
	private Long id;

	@Column(name = "event_id", unique = true, nullable = false, length = 36)
	private String eventId;

	@Column(name = "aggregate_id", nullable = false)
	private Long aggregateId;

	@Enumerated(EnumType.STRING)
	@Column(name = "aggregate_type", nullable = false)
	private AggregateType aggregateType;

	@Enumerated(EnumType.STRING)
	@Column(name = "event_type", nullable = false)
	private EventType eventType;

	@Column(name = "event_data", nullable = false, columnDefinition = "TEXT")
	private String eventData;

	@Enumerated(EnumType.STRING)
	@Column(name = "event_status", nullable = false)
	private EventStatus eventStatus;

	private OutboxMessage(
		String eventId,
		Long aggregateId,
		AggregateType aggregateType,
		EventType eventType,
		String eventData
	) {
		this.eventId = eventId;
		this.aggregateId = aggregateId;
		this.aggregateType = aggregateType;
		this.eventType = eventType;
		this.eventData = eventData;
		this.eventStatus = EventStatus.PENDING;
	}

	public static OutboxMessage create(
		Long aggregateId,
		AggregateType aggregateType,
		EventType eventType,
		String eventData
	) {
		return new OutboxMessage(UUID.randomUUID().toString(), aggregateId, aggregateType, eventType, eventData);
	}

	public void markPublishSucceeded() {
		this.eventStatus = EventStatus.PUBLISHED;
	}

	public void markPublishAttemptFailed() {
		this.eventStatus = EventStatus.FAILED;
	}
}
