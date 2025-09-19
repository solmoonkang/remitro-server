package com.remitro.account.infrastructure.event;

import static com.remitro.common.common.entity.enums.EventStatus.*;

import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.infrastructure.repository.PublishedEventRepository;
import com.remitro.common.common.entity.PublishedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountEventPublisher {

	public static final long FIXED_DELAY_MILLISECONDS = 1000L;
	public static final int BATCH_SIZE = 100;

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final PublishedEventRepository publishedEventRepository;

	@Scheduled(fixedDelay = FIXED_DELAY_MILLISECONDS)
	@Transactional
	public void publishPendingEvents() {
		final List<PublishedEvent> pendingEvents = publishedEventRepository.findPendingEvents(PENDING, BATCH_SIZE);
		validateIfNoEvents(pendingEvents);
		pendingEvents.forEach(this::processAndSendEvent);
	}

	private void processAndSendEvent(PublishedEvent publishedEvent) {
		final ProducerRecord<String, String> producerRecord = new ProducerRecord<>(
			publishedEvent.getEventType().name(), publishedEvent.getEventId(), publishedEvent.getEventData());

		kafkaTemplate.send(producerRecord).thenAccept(success -> {
				publishedEvent.updateEventStatus();
				log.info("[✅ LOGGER] KAFKA 메시지 발행 성공");
			})
			.exceptionally(failure -> {
				log.error("[✅ LOGGER] KAFKA 메시지 발행 실패: {}", failure.getMessage());
				return null;
			});
	}

	private void validateIfNoEvents(List<PublishedEvent> pendingEvents) {
		if (pendingEvents.isEmpty()) {
			log.info("[✅ LOGGER] 발행할 PENDING 이벤트가 없습니다.");
			return;
		}
	}
}
