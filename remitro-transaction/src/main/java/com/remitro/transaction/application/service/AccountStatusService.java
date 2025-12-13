package com.remitro.transaction.application.service;

import org.springframework.stereotype.Service;

import com.remitro.transaction.application.validator.AccountStatusHistoryValidator;
import com.remitro.transaction.domain.event.AccountOpenedEvent;
import com.remitro.transaction.domain.event.AccountStatusUpdatedEvent;
import com.remitro.transaction.domain.model.AccountStatusHistory;
import com.remitro.transaction.domain.repository.StatusHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountStatusService {

	private final AccountStatusHistoryValidator accountStatusHistoryValidator;
	private final StatusHistoryRepository statusHistoryRepository;

	public void recordAccountOpened(String eventId, AccountOpenedEvent accountOpenedEvent) {
		if (statusHistoryRepository.existsByEventId(eventId)) {
			return;
		}

		final AccountStatusHistory accountStatusHistory = AccountStatusHistory.create(
			accountOpenedEvent.accountId(),
			accountOpenedEvent.memberId(),
			eventId,
			null,
			accountOpenedEvent.initialStatus(),
			accountOpenedEvent.actorType(),
			accountOpenedEvent.reasonCode(),
			accountOpenedEvent.occurredAt()
		);

		accountStatusHistoryValidator.validateInitialStatus(accountStatusHistory);

		statusHistoryRepository.save(accountStatusHistory);
	}

	public void recordAccountStatusHistory(String eventId, AccountStatusUpdatedEvent accountStatusUpdatedEvent) {
		if (statusHistoryRepository.existsByEventId(eventId)) {
			return;
		}

		final AccountStatusHistory accountStatusHistory = AccountStatusHistory.create(
			accountStatusUpdatedEvent.accountId(),
			accountStatusUpdatedEvent.memberId(),
			eventId,
			accountStatusUpdatedEvent.previousStatus(),
			accountStatusUpdatedEvent.newStatus(),
			accountStatusUpdatedEvent.actorType(),
			accountStatusUpdatedEvent.reasonCode(),
			accountStatusUpdatedEvent.occurredAt()
		);

		accountStatusHistoryValidator.validateStatusUpdate(accountStatusHistory);

		statusHistoryRepository.save(accountStatusHistory);
	}
}
