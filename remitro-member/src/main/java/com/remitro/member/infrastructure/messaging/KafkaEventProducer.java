package com.remitro.member.infrastructure.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.remitro.event.common.EventEnvelope;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaEventProducer implements EventProducer {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Override
	public <T> void send(EventEnvelope<T> event) {
		kafkaTemplate.send(
			event.aggregateType().name(),
			event.eventId(),
			event
		);
	}
}
