package com.remitro.transaction.infrastructure.messaging.handler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.transaction.application.service.transaction.TransactionService;
import com.remitro.transaction.domain.event.AccountStatusUpdatedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StatusUpdatedEventHandler implements TransactionEventHandler<AccountStatusUpdatedEvent> {

	private final TransactionService transactionService;

	@Override
	@Transactional
	public void handle(String eventId, AccountStatusUpdatedEvent event) {
		transactionService.recordStatusUpdated(eventId, event);
	}
}
