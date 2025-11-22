package com.remitro.member.infrastructure.messaging;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.domain.enums.EventStatus;
import com.remitro.member.domain.model.OutboxMessage;
import com.remitro.member.domain.repository.OutboxMessageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventProducer {

	private static final int PUBLISH_BATCH_SIZE = 100;

	private final OutboxMessageRepository outboxMessageRepository;
	private final KafkaTemplate<String, String> kafkaTemplate;

	@Value("${topics.member-events}")
	private String memberEventTopic;

	@Scheduled(fixedDelayString = "${outbox.member.publish-interval-ms:1000}")
	@Transactional
	public void publishPending() {
		final List<OutboxMessage> pendingMessages = outboxMessageRepository.findByEventStatusOrderByIdAsc(
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
		final String memberEventKey = String.valueOf(message.getAggregateId());
		final String memberEventPayload = message.getEventData();

		try {
			kafkaTemplate.send(memberEventTopic, memberEventKey, memberEventPayload).get();

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
