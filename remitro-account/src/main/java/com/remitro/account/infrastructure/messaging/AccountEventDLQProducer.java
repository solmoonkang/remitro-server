package com.remitro.account.infrastructure.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.remitro.common.contract.event.EventEnvelope;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountEventDLQProducer {

	private final KafkaEventProducer kafkaEventProducer;

	@Value("${topics.account-events-dlq}")
	private String accountEventDlqTopic;

	public void sendMessageToDLQ(EventEnvelope eventEnvelope) {
		kafkaEventProducer.send(accountEventDlqTopic, eventEnvelope);

		log.error("[✅ LOGGER] KAFKA 메시지 발행 실패, DLQ로 이동 (EVENT ID = {})",
			eventEnvelope.eventId()
		);
	}
}
