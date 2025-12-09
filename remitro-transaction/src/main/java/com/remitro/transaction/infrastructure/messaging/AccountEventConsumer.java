package com.remitro.transaction.infrastructure.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import com.remitro.common.contract.event.EventEnvelope;
import com.remitro.transaction.infrastructure.messaging.dispatcher.TransactionEventDispatcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountEventConsumer {

	private final TransactionEventDispatcher transactionEventDispatcher;

	@KafkaListener(
		topics = "${topics.account-events}",
		groupId = "${spring.kafka.consumer.group-id}",
		concurrency = "5"
	)
	@RetryableTopic(
		attempts = "5",
		dltTopicSuffix = "-dlq",
		backoff = @Backoff(delay = 1000, maxDelay = 16000, multiplier = 2)
	)
	public void handleAccountEvent(
		EventEnvelope eventEnvelope,
		@Header(KafkaHeaders.RECEIVED_KEY) Object kafkaKey
	) {
		log.info("[✅ LOGGER] ACCOUNT 이벤트를 수신했습니다: KEY={}, EVENT_ID={}, EVENT_TYPE={}",
			kafkaKey,
			eventEnvelope.eventId(),
			eventEnvelope.eventType()
		);

		transactionEventDispatcher.dispatch(eventEnvelope);
	}
}
