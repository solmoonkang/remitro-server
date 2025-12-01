package com.remitro.account.application.service.outbox;

import org.springframework.stereotype.Service;

import com.remitro.account.application.mapper.AccountEventMapper;
import com.remitro.account.domain.enums.AggregateType;
import com.remitro.account.domain.event.EventType;
import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.OutboxMessage;
import com.remitro.account.domain.enums.AccountStatus;
import com.remitro.account.domain.repository.OutboxMessageRepository;
import com.remitro.common.contract.account.AccountDepositEvent;
import com.remitro.common.contract.account.AccountStatusChangedEvent;
import com.remitro.common.util.JsonMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountOutboxService {

	private final OutboxMessageRepository outboxMessageRepository;

	public void appendAccountStatusUpdatedEvent(Account account, AccountStatus previousStatus) {
		final AccountStatusChangedEvent accountStatusChangedEvent = AccountEventMapper.toAccountStatusChangedEvent(
			account,
			previousStatus
		);

		final OutboxMessage accountOutbox = OutboxMessage.create(
			account.getId(),
			AggregateType.ACCOUNT,
			EventType.ACCOUNT_STATUS_UPDATED,
			JsonMapper.toJSON(accountStatusChangedEvent)
		);

		outboxMessageRepository.save(accountOutbox);
	}

	public void appendDepositEvent(Account account, Long amount, String description) {
		final AccountDepositEvent accountDepositEvent = AccountEventMapper.toAccountDepositEvent(
			account,
			amount,
			description
		);

		final OutboxMessage accountOutbox = OutboxMessage.create(
			account.getId(),
			AggregateType.ACCOUNT,
			EventType.ACCOUNT_BALANCE_UPDATED,
			JsonMapper.toJSON(accountDepositEvent)
		);

		outboxMessageRepository.save(accountOutbox);
	}
}
