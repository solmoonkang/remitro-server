package com.remitro.transaction.application.service.transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.transaction.application.validator.SchemaVersionValidator;
import com.remitro.transaction.domain.event.AccountDepositEvent;
import com.remitro.transaction.domain.event.AccountOpenedEvent;
import com.remitro.transaction.domain.event.AccountStatusUpdatedEvent;
import com.remitro.transaction.domain.transaction.model.Transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

	private final SchemaVersionValidator schemaVersionValidator;
	private final TransactionReadService transactionReadService;
	private final TransactionWriteService transactionWriteService;
	private final LedgerService ledgerService;
	private final AccountStatusService accountStatusService;

	@Transactional
	public void recordAccountOpened(String eventId, AccountOpenedEvent accountOpenedEvent) {
		schemaVersionValidator.validateAccountOpened(accountOpenedEvent.schemaVersion());
		accountStatusService.recordAccountOpened(eventId, accountOpenedEvent);
	}

	@Transactional
	public void recordStatusUpdated(String eventId, AccountStatusUpdatedEvent accountStatusUpdatedEvent) {
		schemaVersionValidator.validateAccountStatusUpdated(accountStatusUpdatedEvent.schemaVersion());
		accountStatusService.recordAccountStatusHistory(eventId, accountStatusUpdatedEvent);
	}

	@Transactional
	public void recordDeposit(String eventId, AccountDepositEvent accountDepositEvent) {
		if (transactionReadService.existsTransactionEvent(eventId)) {
			return;
		}

		final Long previousBalance = transactionReadService.findLatestBalance(accountDepositEvent.accountId());
		final Long balanceAfter = previousBalance + accountDepositEvent.amount();

		final Transaction transaction = transactionWriteService.saveTransaction(eventId, accountDepositEvent);
		ledgerService.saveLedgerEntry(transaction, balanceAfter, accountDepositEvent);
	}
}
