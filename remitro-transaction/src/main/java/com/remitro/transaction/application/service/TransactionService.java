package com.remitro.transaction.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.contract.account.AccountDepositEvent;
import com.remitro.common.contract.account.AccountStatusChangedEvent;
import com.remitro.transaction.domain.model.Transaction;
import com.remitro.transaction.domain.model.enums.LedgerDirection;
import com.remitro.transaction.domain.model.enums.TransactionType;

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
	public void recordStatusChangedTransaction(String eventId, AccountStatusChangedEvent accountStatusChangedEvent) {
		if (transactionReadService.existsStatusChangeEvent(eventId)) {
			return;
		}

		transactionWriteService.saveAccountStatusHistory(eventId, accountStatusChangedEvent);
	}

	@Transactional
	public void recordDepositTransaction(String eventId, AccountDepositEvent accountDepositEvent) {
		if (transactionReadService.existsTransactionEvent(eventId)) {
			return;
		}

		final Transaction transaction = transactionWriteService.saveTransaction(
			eventId,
			TransactionType.DEPOSIT,
			accountDepositEvent
		);
		transactionWriteService.saveLedgerEntry(
			transaction,
			LedgerDirection.CREDIT,
			accountDepositEvent.balanceAfter(),
			accountDepositEvent
		);
	}
}
