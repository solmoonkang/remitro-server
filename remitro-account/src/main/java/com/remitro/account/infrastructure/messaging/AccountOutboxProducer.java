package com.remitro.account.infrastructure.messaging;

import static com.remitro.common.infra.util.KafkaConstant.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
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
public class AccountOutboxProducer {

	public static final int OUTBOX_BATCH_SIZE = 100;
	public static final int MAX_KAFKA_PUBLISH_ATTEMPTS = 5;
	public static final long RETRY_BACKOFF_MILLS = 2000;

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final OutboxMessageRepository outboxMessageRepository;

	@Value("${topics.account-events}")
	private String accountEventTopic;

	@Value("${topics.account-events-dlq}")
	private String accountEventDlqTopic;

	@Scheduled(fixedDelayString = "${outbox.account.publish-interval-ms:1000}")
	@Transactional
	public void publishPending() {
		final List<OutboxMessage> pendingMessages = outboxMessageRepository.findByEventStatusOrderByCreatedAtAsc(
			EventStatus.PENDING,
			PageRequest.of(0, OUTBOX_BATCH_SIZE)
		);

		if (pendingMessages.isEmpty()) {
			log.debug("[✅ LOGGER] 발행할 메시지가 없습니다.");
			return;
		}

		log.info("[✅ LOGGER] PENDING 메시지 {}건 발행 시도",
			pendingMessages.size()
		);

		for (OutboxMessage message : pendingMessages) {
			publishSingleOutboxMessage(message);
		}
	}

	private void publishSingleOutboxMessage(OutboxMessage message) {
		final ProducerRecord<String, String> producerRecord = buildProducerRecord(message);

		int attempts = 0;

		while (attempts < MAX_KAFKA_PUBLISH_ATTEMPTS) {
			attempts++;

			try {
				kafkaTemplate.send(producerRecord).get();
				markOutboxMessageAsPublished(message);

				log.info("[✅ LOGGER] KAFKA 메시지 발행 성공, "
						+ "(EVENT TYPE = {}, EVENT ID = {}, AGGREGATE ID = {})",
					message.getEventType(),
					message.getEventId(),
					message.getAggregateId()
				);
				return;

			} catch (Exception e) {
				log.warn("[✅ LOGGER] KAFKA 메시지 발행 실패, "
						+ "(발행 재시도 횟수 {}/{}): {}",
					attempts,
					MAX_KAFKA_PUBLISH_ATTEMPTS,
					e.getMessage()
				);

				sleep(RETRY_BACKOFF_MILLS * attempts);
			}
		}

		sendMessageToDlq(message);
		markOutboxMessageAsFailed(message);

		log.error("[✅ LOGGER] KAFKA 메시지 발행 실패, "
				+ "DLQ로 이동 (EVENT ID = {})",
			message.getEventData()
		);
	}

	private ProducerRecord<String, String> buildProducerRecord(OutboxMessage message) {
		ProducerRecord<String, String> producerRecord = new ProducerRecord<>(
			accountEventTopic,
			String.valueOf(message.getAggregateId()),
			message.getEventData()
		);

		producerRecord.headers().add(
			EVENT_HEADER_ID,
			message.getEventId().getBytes(StandardCharsets.UTF_8)
		);

		producerRecord.headers().add(
			EVENT_HEADER_TYPE,
			message.getEventType().name().getBytes(StandardCharsets.UTF_8)
		);

		return producerRecord;
	}

	private void sendMessageToDlq(OutboxMessage message) {
		kafkaTemplate.send(accountEventDlqTopic, String.valueOf(message.getAggregateId()), message.getEventData());
	}

	private void markOutboxMessageAsPublished(OutboxMessage message) {
		message.markPublishSucceeded();
		outboxMessageRepository.save(message);
	}

	private void markOutboxMessageAsFailed(OutboxMessage message) {
		message.markPublishAttemptFailed();
		outboxMessageRepository.save(message);
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
