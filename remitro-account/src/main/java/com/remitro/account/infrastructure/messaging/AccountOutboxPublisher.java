package com.remitro.account.infrastructure.messaging;

import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.domain.model.OutboxMessage;
import com.remitro.account.domain.repository.OutboxMessageRepository;
import com.remitro.common.domain.enums.EventStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountOutboxPublisher {

	public static final int PUBLISH_BATCH_SIZE = 100;

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final OutboxMessageRepository outboxMessageRepository;

	@Scheduled(fixedDelayString = "${outbox.account.publish-interval-ms:1000}")
	@Transactional
	public void publishPendingMessages() {
		final List<OutboxMessage> pendingEvents = outboxMessageRepository.findByEventStatusOrderByCreatedAtAsc(
			EventStatus.PENDING,
			PageRequest.of(0, PUBLISH_BATCH_SIZE)
		);

		if (pendingEvents.isEmpty()) {
			log.info("[✅ LOGGER] 발행할 PENDING 이벤트가 없습니다.");
			return;
		}

		for (OutboxMessage outboxMessage : pendingEvents) {
			processAndSendEvent(outboxMessage);
		}

		log.info("[✅ LOGGER] PENDING 이벤트 발행 처리를 완료했습니다.");
	}

	private void processAndSendEvent(OutboxMessage outboxMessage) {
		final ProducerRecord<String, String> producerRecord = new ProducerRecord<>(
			outboxMessage.getEventType().name(),
			outboxMessage.getEventId(),
			outboxMessage.getEventData()
		);

		try {
			kafkaTemplate.send(producerRecord).get();
			outboxMessage.markPublished();
			outboxMessageRepository.save(outboxMessage);
			log.info("[✅ LOGGER] KAFKA 메시지 발행 성공");
		} catch (Exception e) {
			log.error("[✅ LOGGER] KAFKA 메시지 발행 실패: {}", e.getMessage());
		}
	}
}
