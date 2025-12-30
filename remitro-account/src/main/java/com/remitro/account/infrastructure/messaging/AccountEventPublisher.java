package com.remitro.account.infrastructure.messaging;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.remitro.account.application.common.mapper.AccountEventMapper;
import com.remitro.account.domain.common.enums.ActorType;
import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.loan.model.Loan;
import com.remitro.account.domain.outbox.model.OutboxMessage;
import com.remitro.account.domain.outbox.repository.OutboxMessageRepository;
import com.remitro.account.infrastructure.support.JsonMapper;
import com.remitro.event.domain.common.metadata.AggregateType;
import com.remitro.event.domain.common.metadata.EventType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountEventPublisher {

	private final OutboxMessageRepository outboxMessageRepository;

	public void publishAccountOpened(Account account, LocalDateTime now) {
		publish(
			account.getId(),
			EventType.ACCOUNT_OPENED,
			AccountEventMapper.toAccountOpenedEvent(account, ActorType.MEMBER, null, now)
		);
	}

	public void publishLoanCreated(Loan loan, LocalDateTime now) {
		publish(
			loan.getId(),
			EventType.LOAN_CREATED,
			AccountEventMapper.toLoanCreatedEvent(loan, now)
		);
	}

	private void publish(Long aggregateId, EventType eventType, Object payload) {
		final OutboxMessage outboxMessage = OutboxMessage.create(
			aggregateId,
			AggregateType.ACCOUNT,
			eventType,
			JsonMapper.toJSON(payload)
		);

		outboxMessageRepository.save(outboxMessage);
	}
}
