package com.remitro.account.application.service;

import org.springframework.stereotype.Service;

import com.remitro.account.application.mapper.AccountEventMapper;
import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.OutboxMessage;
import com.remitro.account.domain.repository.OutboxMessageRepository;
import com.remitro.common.contract.account.AccountDepositEvent;
import com.remitro.common.domain.enums.AggregateType;
import com.remitro.common.domain.enums.EventType;
import com.remitro.common.infra.util.JsonUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountOutboxService {

	private final OutboxMessageRepository outboxMessageRepository;

	public void appendDepositEvent(Account account, Long amount) {
		final AccountDepositEvent accountDepositEvent = AccountEventMapper.toAccountDepositEvent(account, amount);
		final OutboxMessage accountOutbox = OutboxMessage.create(
			account.getId(),
			AggregateType.ACCOUNT,
			EventType.DEPOSIT_EVENT,
			JsonUtil.toJSON(accountDepositEvent)
		);
		outboxMessageRepository.save(accountOutbox);
	}
}
