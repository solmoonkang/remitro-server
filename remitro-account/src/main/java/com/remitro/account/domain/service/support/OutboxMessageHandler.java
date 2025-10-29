package com.remitro.account.domain.service.support;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.dto.request.OutboxMessageRequest;
import com.remitro.account.infrastructure.outbox.model.OutboxMessage;
import com.remitro.account.infrastructure.outbox.repository.OutboxMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutboxMessageHandler {

	private final OutboxMessageRepository outboxMessageRepository;

	@Transactional(propagation = Propagation.MANDATORY)
	public void recordOutboxMessage(OutboxMessageRequest outboxMessageRequest) {
		final OutboxMessage outboxMessage = OutboxMessage.createOutboxMessage(
			outboxMessageRequest.eventId(),
			outboxMessageRequest.aggregateId(),
			outboxMessageRequest.aggregateType(),
			outboxMessageRequest.eventType(),
			outboxMessageRequest.eventData()
		);

		outboxMessageRepository.save(outboxMessage);
	}
}
