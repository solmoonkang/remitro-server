package com.remitro.common.common.entity;

import com.remitro.common.common.entity.enums.AggregateType;
import com.remitro.common.common.entity.enums.EventStatus;
import com.remitro.common.common.entity.enums.EventType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "PUBLISHED_EVENTS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublishedEvent extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "published_event_id", nullable = false)
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

	private PublishedEvent(String eventId, Long aggregateId, AggregateType aggregateType, EventType eventType,
		String eventData) {

		this.eventId = eventId;
		this.aggregateId = aggregateId;
		this.aggregateType = aggregateType;
		this.eventType = eventType;
		this.eventData = eventData;
		this.eventStatus = EventStatus.PENDING;
	}

	public static PublishedEvent createPublishedEvent(String eventId, Long aggregateId, AggregateType aggregateType,
		EventType eventType, String eventData) {

		return new PublishedEvent(eventId, aggregateId, aggregateType, eventType, eventData);
	}

	public void updateEventStatus() {
		this.eventStatus = EventStatus.PUBLISHED;
	}
}
