package com.remitro.account.infrastructure.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.remitro.common.contract.event.EventEnvelope;
import com.remitro.common.util.JsonMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;

	public void send(String eventTopic, EventEnvelope eventEnvelope) {
		try {
			final String eventMessage = JsonMapper.toJSON(eventEnvelope);
			kafkaTemplate.send(eventTopic, eventEnvelope.eventId(), eventMessage);

			log.info("[✅ LOGGER] KAFKA 메시지 발행에 성공했습니다: EVENT_ID={}, EVENT_TYPE={}",
				eventEnvelope.eventId(),
				eventEnvelope.eventType()
			);

		} catch (Exception e) {
			log.error("[✅ LOGGER] KAFKA 메시지 발행에 실패했습니다: EVENT_ID={}, REASON={}",
				eventEnvelope.eventId(),
				e.getMessage()
			);
		}
	}
}

