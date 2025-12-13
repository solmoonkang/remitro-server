package com.remitro.transaction.infrastructure.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.remitro.common.contract.event.EventEnvelope;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountEventDLQConsumer {

	@KafkaListener(
		topics = "${topics.account-events}" + "-dlq",
		groupId = "${spring.kafka.consumer.group-id}" + "-dlq",
		concurrency = "3"
	)
	public void handleAccountDLQEvent(EventEnvelope eventEnvelope) {
		log.error("[✅ LOGGER] ACCOUNT DLQ 이벤트를 수신했습니다: EVENT_ID={}, EVENT_TYPE={}",
			eventEnvelope.eventId(),
			eventEnvelope.eventType()
		);
	}
}
