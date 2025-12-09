package com.remitro.transaction.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.transaction.domain.event.AccountDepositEvent;
import com.remitro.transaction.domain.event.AccountStatusUpdatedEvent;
import com.remitro.transaction.domain.model.Transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

	private final TransactionReadService transactionReadService;
	private final TransactionWriteService transactionWriteService;

	@Transactional
	public void recordDeposit(String eventId, AccountDepositEvent accountDepositEvent) {
		if (transactionReadService.existsTransactionEvent(eventId)) {
			return;
		}

		final Long previousBalance = transactionReadService.findLatestBalance(accountDepositEvent.accountId());
		final Long balanceAfter = previousBalance + accountDepositEvent.amount();

		final Transaction transaction = transactionWriteService.saveTransaction(eventId, accountDepositEvent);
		transactionWriteService.saveLedgerEntry(transaction, balanceAfter, accountDepositEvent);
	}

	@Transactional
	public void recordStatusUpdated(String eventId, AccountStatusUpdatedEvent accountStatusUpdatedEvent) {
		if (transactionReadService.existsStatusUpdatedEvent(eventId)) {
			return;
		}

		transactionWriteService.saveAccountStatusHistory(eventId, accountStatusUpdatedEvent);
	}
}
