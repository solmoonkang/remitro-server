package com.remitro.transaction.infrastructure.messaging.handler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.transaction.application.service.transaction.TransactionService;
import com.remitro.transaction.domain.event.AccountOpenedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountOpenedEventHandler implements TransactionEventHandler<AccountOpenedEvent> {

	private final TransactionService transactionService;

	@Override
	@Transactional
	public void handle(String eventId, AccountOpenedEvent event) {
		transactionService.recordAccountOpened(eventId, event);
	}
}
