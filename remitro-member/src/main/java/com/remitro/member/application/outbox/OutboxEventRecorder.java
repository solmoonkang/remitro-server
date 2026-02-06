package com.remitro.member.application.outbox;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remitro.event.common.EventEnvelope;
import com.remitro.event.common.EventMetadata;
import com.remitro.event.common.EventType;
import com.remitro.member.domain.outbox.model.OutboxEvent;
import com.remitro.member.domain.outbox.repository.OutboxEventRepository;
import com.remitro.support.util.JsonSupport;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OutboxEventRecorder {

	private static final String MDC_TRACE_ID = "traceId";

	private final OutboxEventRepository outboxEventRepository;
	private final ObjectMapper objectMapper;
	private final Clock clock;

	@Value("${spring.application.name}")
	private String applicationName;

	@Transactional(propagation = Propagation.MANDATORY)
	public <T> void record(EventType eventType, Long aggregateId, T payload) {
		final LocalDateTime now = LocalDateTime.now(clock);

		final EventMetadata eventMetadata = new EventMetadata(
			UUID.randomUUID().toString(),
			now,
			eventType.getSchemaVersion(),
			MDC.get(MDC_TRACE_ID),
			UUID.randomUUID().toString(),
			applicationName
		);

		final EventEnvelope<T> eventEnvelope = new EventEnvelope<>(
			eventMetadata,
			eventType,
			String.valueOf(aggregateId),
			payload
		);

		final OutboxEvent outboxEvent = OutboxEvent.newEvent(
			"MEMBER",
			aggregateId,
			eventType,
			JsonSupport.toJSON(objectMapper, eventEnvelope),
			now
		);
		outboxEventRepository.save(outboxEvent);
	}
}
