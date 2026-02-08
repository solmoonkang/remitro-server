package com.remitro.member.domain.outbox.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;

import com.remitro.event.common.EventType;
import com.remitro.member.domain.outbox.enums.OutboxStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
	name = "OUTBOX_EVENTS",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_outbox_event_id", columnNames = "event_id")
	},
	indexes = {
		@Index(name = "idx_outbox_status_retry_count", columnList = "outbox_status, retry_count")
	}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "outbox_event_id")
	private Long id;

	@Comment("")
	@Column(name = "aggregate_type", nullable = false, length = 64)
	private String aggregateType;

	@Comment("")
	@Column(name = "aggregate_id", nullable = false, length = 200)
	private String aggregateId;

	@Comment("이벤트 타입")
	@Column(name = "event_type", nullable = false, length = 100)
	private String eventType;

	@Comment("이벤트 JSON (Envelope 직렬화)")
	@Lob
	@Column(name = "message_json", nullable = false)
	private String messageJson;

	@Comment("아웃박스 상태")
	@Enumerated(EnumType.STRING)
	@Column(name = "outbox_status", nullable = false, length = 20)
	private OutboxStatus outboxStatus;

	@Comment("재시도 횟수")
	@Column(name = "retry_count", nullable = false)
	private int retryCount;

	@Comment("마지막 실패 원인")
	@Column(name = "error_message", length = 4000)
	private String errorMessage;

	@Comment("생성 일시")
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Comment("마지막 시도 일시")
	@Column(name = "last_attempt_at")
	private LocalDateTime lastAttemptAt;

	private OutboxEvent(
		String aggregateType,
		String aggregateId,
		String eventType,
		String messageJson,
		LocalDateTime createdAt
	) {
		this.aggregateType = aggregateType;
		this.aggregateId = aggregateId;
		this.eventType = eventType;
		this.messageJson = messageJson;
		this.outboxStatus = OutboxStatus.PENDING;
		this.createdAt = createdAt;
	}

	public static OutboxEvent newEvent(
		String aggregateType,
		Long aggregateId,
		EventType eventType,
		String messageJson,
		LocalDateTime now
	) {
		return new OutboxEvent(
			aggregateType,
			String.valueOf(aggregateId),
			eventType.getCode(),
			messageJson, now
		);
	}

	public void publish(LocalDateTime now) {
		this.outboxStatus = OutboxStatus.PUBLISHED;
		this.lastAttemptAt = now;
	}

	public void fail(String errorMessage, LocalDateTime now) {
		this.retryCount++;
		this.lastAttemptAt = now;
		this.errorMessage = errorMessage;

		if (this.retryCount >= 5) {
			this.outboxStatus = OutboxStatus.FAILED;
		}
	}
}
