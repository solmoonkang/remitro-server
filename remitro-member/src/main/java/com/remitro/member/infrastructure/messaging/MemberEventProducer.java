package com.remitro.member.infrastructure.messaging;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.messaging.EventEnvelope;
import com.remitro.member.domain.event.EventStatus;
import com.remitro.member.domain.model.OutboxMessage;
import com.remitro.member.domain.repository.OutboxMessageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventProducer {

	private final OutboxMessageRepository outboxMessageRepository;
	private final KafkaEventProducer kafkaEventProducer;

	@Value("${topics.member-events}")
	private String memberEventTopic;

	@Scheduled(fixedDelayString = "${outbox.member.publish-interval-ms:1000}")
	public void publishPending() {
		List<OutboxMessage> pendingMessages = outboxMessageRepository.findByEventStatusOrderByIdAsc(
			EventStatus.PENDING
		);

		if (pendingMessages.isEmpty()) {
			log.info("[✅ LOGGER] OUTBOX_MESSAGE에서 발행할 PENDING 이벤트가 없습니다.");
			return;
		}

		pendingMessages.forEach(this::sendAndMarkPublished);

		log.info("[✅ LOGGER] OUTBOX_MESSAGE에서 {}건 발행을 완료했습니다.",
			pendingMessages.size()
		);
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
			kafkaEventProducer.send(memberEventTopic, eventEnvelope);

			outboxMessage.markPublished();
			outboxMessageRepository.save(outboxMessage);

		} catch (Exception e) {
			log.error("[✅ ERROR] OUTBOX_MESSAGE에서 이벤트 발행을 실패했습니다: EVENT_ID={}",
				outboxMessage.getEventId(), e
			);
		}
	}
}
