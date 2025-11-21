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

	public static final int PUBLISH_BATCH_SIZE = 100;

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final OutboxMessageRepository outboxMessageRepository;

	@Value("${topics.account-events}")
	private String accountEventTopic;

	@Scheduled(fixedDelayString = "${outbox.account.publish-interval-ms:1000}")
	@Transactional
	public void publishPending() {
		final List<OutboxMessage> pendingMessages = outboxMessageRepository.findByEventStatusOrderByCreatedAtAsc(
			EventStatus.PENDING,
			PageRequest.of(0, PUBLISH_BATCH_SIZE)
		);

		if (pendingMessages.isEmpty()) {
			log.info("[✅ LOGGER] 발행할 PENDING 이벤트가 없습니다.");
			return;
		}

		for (OutboxMessage message : pendingMessages) {
			sendAndMarkPublished(message);
		}

		log.info("[✅ LOGGER] PENDING 이벤트 발행 처리를 완료했습니다. "
				+ "COUNT = {}",
			pendingMessages.size()
		);
	}

	private void sendAndMarkPublished(OutboxMessage message) {
		final String accountEventKey = String.valueOf(message.getAggregateId());
		final String accountEventPayload = message.getEventData();

		final ProducerRecord<String, String> producerRecord = new ProducerRecord<>(
			accountEventTopic,
			accountEventKey,
			accountEventPayload
		);

		producerRecord.headers().add(
			EVENT_HEADER_ID,
			message.getEventId().getBytes(StandardCharsets.UTF_8)
		);

		producerRecord.headers().add(
			EVENT_HEADER_TYPE,
			message.getEventType().name().getBytes(StandardCharsets.UTF_8)
		);

		try {
			kafkaTemplate.send(producerRecord).get();

			message.markPublished();
			outboxMessageRepository.save(message);

			log.info("[✅ LOGGER] KAFKA 메시지 발행 성공, "
					+ "(EVENT TYPE = {}, EVENT ID = {}, AGGREGATE ID = {})",
				message.getEventType(),
				message.getEventId(),
				message.getAggregateId()
			);
		} catch (Exception e) {
			log.error("[✅ LOGGER] KAFKA 메시지 발행 실패, "
					+ "(EVENT TYPE = {}, EVENT ID = {}, AGGREGATE ID = {}): {}",
				message.getEventType(),
				message.getEventId(),
				message.getAggregateId(),
				e.getMessage()
			);
		}
	}
}
