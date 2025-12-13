package com.remitro.account.application.service.outbox;

import org.springframework.stereotype.Service;

import com.remitro.account.application.mapper.AccountEventMapper;
import com.remitro.account.domain.enums.AccountStatus;
import com.remitro.account.domain.enums.AccountStatusUpdateReason;
import com.remitro.account.domain.enums.ActorType;
import com.remitro.account.domain.enums.AggregateType;
import com.remitro.account.domain.event.EventType;
import com.remitro.account.domain.event.account.AccountDepositEvent;
import com.remitro.account.domain.event.account.AccountOpenedEvent;
import com.remitro.account.domain.event.account.AccountStatusUpdatedEvent;
import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.OutboxMessage;
import com.remitro.account.domain.repository.OutboxMessageRepository;
import com.remitro.common.util.JsonMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountOutboxService {

	private final OutboxMessageRepository outboxMessageRepository;

	public void appendAccountOpenedEventOutbox(Account account) {
		final AccountOpenedEvent accountOpenedEvent = AccountEventMapper.toAccountOpenedEvent(
			account,
			ActorType.MEMBER,
			AccountStatusUpdateReason.USER_REQUEST
		);

		final OutboxMessage outboxMessage = OutboxMessage.create(
			account.getId(),
			AggregateType.ACCOUNT,
			EventType.ACCOUNT_OPENED,
			JsonMapper.toJSON(accountOpenedEvent)
		);

		outboxMessageRepository.save(outboxMessage);
	}

	public void appendAccountStatusUpdatedEventOutbox(
		Account account,
		AccountStatus previousStatus,
		ActorType actorType,
		AccountStatusUpdateReason accountStatusUpdateReason
	) {
		final AccountStatusUpdatedEvent accountStatusUpdatedEvent = AccountEventMapper.toAccountStatusUpdatedEvent(
			account,
			previousStatus,
			actorType,
			accountStatusUpdateReason
		);

		final OutboxMessage outboxMessage = OutboxMessage.create(
			account.getId(),
			AggregateType.ACCOUNT,
			EventType.ACCOUNT_STATUS_UPDATED,
			JsonMapper.toJSON(accountStatusUpdatedEvent)
		);

		outboxMessageRepository.save(outboxMessage);
	}

	public void appendDepositEventOutbox(Account account, Long amount, String description) {
		final AccountDepositEvent accountDepositEvent = AccountEventMapper.toAccountDepositEvent(
			account,
			amount,
			description
		);

		final OutboxMessage outboxMessage = OutboxMessage.create(
			account.getId(),
			AggregateType.ACCOUNT,
			EventType.ACCOUNT_DEPOSITED,
			JsonMapper.toJSON(accountDepositEvent)
		);

		outboxMessageRepository.save(outboxMessage);
	}
}
