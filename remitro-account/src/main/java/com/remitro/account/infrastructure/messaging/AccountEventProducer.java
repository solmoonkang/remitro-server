package com.remitro.account.infrastructure.messaging;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.domain.event.EventStatus;
import com.remitro.account.domain.model.OutboxMessage;
import com.remitro.account.domain.repository.OutboxMessageRepository;
import com.remitro.common.contract.event.EventEnvelope;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountEventProducer {

	private final KafkaEventProducer kafkaEventProducer;
	private final OutboxMessageRepository outboxMessageRepository;
	private final AccountEventDLQProducer accountEventDLQProducer;

	@Value("${topics.account-events}")
	private String accountEventTopic;

	@Scheduled(fixedDelayString = "${outbox.account.publish-interval-ms:1000}")
	public void publishPending() {
		final List<OutboxMessage> pendingMessages = outboxMessageRepository.findByEventStatusOrderByCreatedAtAsc(
			EventStatus.PENDING
		);

		pendingMessages.forEach(this::sendAndMarkPublished);
	}

	@Transactional
	public void sendAndMarkPublished(OutboxMessage outboxMessage) {
		final EventEnvelope eventEnvelope = new EventEnvelope(
			outboxMessage.getEventId(),
			outboxMessage.getEventType().name(),
			outboxMessage.getAggregateType().name(),
			outboxMessage.getAggregateId(),
			outboxMessage.getCreatedAt(),
			outboxMessage.getEventData()
		);

		try {
			kafkaEventProducer.send(accountEventTopic, eventEnvelope);
			outboxMessage.markPublishSucceeded();

		} catch (Exception e) {
			log.error("[✅ ERROR] OUTBOX_MESSAGE에서 이벤트 발행을 실패했습니다: EVENT_ID={}",
				outboxMessage.getEventId(), e
			);
			outboxMessage.markPublishAttemptFailed();
			accountEventDLQProducer.sendMessageToDLQ(eventEnvelope);
		}
	}
}
