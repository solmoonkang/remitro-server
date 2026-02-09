package com.remitro.member.application.batch.outbox;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import com.remitro.member.application.outbox.OutboxEventService;
import com.remitro.member.domain.outbox.model.OutboxEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxBatchScheduler {

	private final OutboxEventService outboxEventService;
	private final TransactionTemplate transactionTemplate;
	private final OutboxBatchProperties outboxBatchProperties;

	@Scheduled(fixedDelayString = "${batch.outbox.fixed-delay}")
	public void executeOutboxEventPublishing() {
		transactionTemplate.execute(status -> {
			final List<OutboxEvent> pendingEvents = outboxEventService.findAllPendingEvents(
				outboxBatchProperties.chunkSize()
			);

			if (pendingEvents.isEmpty())
				return null;

			log.info("[✅ LOGGER] 미발행 아웃박스 이벤트 발행 배치를 시작합니다. (대상 건수: {}건)", pendingEvents.size());

			pendingEvents.forEach(outboxEventService::process);

			log.info("[✅ LOGGER] 미발행 아웃박스 이벤트 발행 배치를 완료했습니다.");

			return null;
		});
	}
}
