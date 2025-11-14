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
public class MemberOutboxPublisher {

	private static final int PUBLISH_BATCH_SIZE = 500;

	private final OutboxMessageRepository outboxMessageRepository;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Value("${topics.member-status-changed}")
	private String memberStatusChangedTopic;

	@Scheduled(fixedDelayString = "${outbox.publish-interval-ms:1000}")
	@Transactional
	public void publishPending() {
		final List<OutboxMessage> outboxMessages = outboxMessageRepository.findByEventStatusOrderByIdAsc(
			EventStatus.PENDING, PageRequest.of(0, PUBLISH_BATCH_SIZE)
		);

		for (OutboxMessage message : outboxMessages) {
			try {
				kafkaTemplate.send(
					memberStatusChangedTopic, String.valueOf(message.getAggregateId()), message.getEventData());

				message.markPublished();
				outboxMessageRepository.save(message);
			} catch (Exception e) {
				log.error("[✅ LOGGER] KAFKA 메시지 발행에 실패했습니다. (eventId={}, aggregateId={}): {}",
					message.getEventId(),
					message.getAggregateId(),
					e.getMessage()
				);
			}
		}
	}
}
