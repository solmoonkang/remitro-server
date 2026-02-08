package com.remitro.member.application.outbox;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.remitro.event.common.EventType;
import com.remitro.member.domain.outbox.model.OutboxEvent;
import com.remitro.member.domain.outbox.port.EventProducer;
import com.remitro.member.domain.outbox.repository.OutboxEventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxEventService {

	private final OutboxEventRepository outboxEventRepository;
	private final EventProducer eventProducer;
	private final Clock clock;

	public void process(OutboxEvent outboxEvent) {
		final LocalDateTime now = LocalDateTime.now(clock);

		try {
			final EventType eventType = EventType.from(outboxEvent.getEventType());

			eventProducer.produce(
					eventType.getEventTopic().getValue(),
					outboxEvent.getAggregateId(),
					outboxEvent.getMessageJson()
				)
				.join();

			outboxEvent.publish(now);
			log.info("[✅ LOGGER] 아웃박스 이벤트 발행이 완료되었습니다. (OUTBOX_EVENT_ID = {})", outboxEvent.getId());

		} catch (Exception e) {
			log.error("[❎ LOGGER] 이벤트 발행에 실패해 재시도 처리를 합니다. (OUTBOX_EVENT_ID = {}, ERROR_MESSAGE = {})",
				outboxEvent.getId(), e.getMessage()
			);
			outboxEvent.fail(e.getMessage(), now);
		}
	}

	public List<OutboxEvent> findAllPendingEvents(int limit) {
		return outboxEventRepository.findAllPendingEventsForUpdate(limit);
	}
}
