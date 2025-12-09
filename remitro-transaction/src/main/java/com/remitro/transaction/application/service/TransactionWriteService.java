package com.remitro.transaction.application.service;

import org.springframework.stereotype.Service;

import com.remitro.transaction.domain.enums.LedgerDirection;
import com.remitro.transaction.domain.enums.TransactionType;
import com.remitro.transaction.domain.event.AccountStatusUpdatedEvent;
import com.remitro.transaction.domain.event.AccountTransactionEvent;
import com.remitro.transaction.domain.model.AccountStatusHistory;
import com.remitro.transaction.domain.model.LedgerEntry;
import com.remitro.transaction.domain.model.Transaction;
import com.remitro.transaction.domain.repository.LedgerEntryRepository;
import com.remitro.transaction.domain.repository.StatusHistoryRepository;
import com.remitro.transaction.domain.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionWriteService {

	private final TransactionRepository transactionRepository;
	private final LedgerEntryRepository ledgerEntryRepository;
	private final StatusHistoryRepository statusHistoryRepository;

	public Transaction saveTransaction(String eventId, AccountTransactionEvent accountTransactionEvent) {
		final Transaction transaction = Transaction.create(
			accountTransactionEvent.accountId(),
			eventId,
			accountTransactionEvent.transactionType(),
			accountTransactionEvent.amount(),
			accountTransactionEvent.description(),
			accountTransactionEvent.occurredAt()
		);

		return transactionRepository.save(transaction);
	}

	public void saveLedgerEntry(
		Transaction transaction,
		Long balanceAfter,
		AccountTransactionEvent accountTransactionEvent
	) {
		final LedgerEntry ledgerEntry = LedgerEntry.create(
			transaction,
			resolveLedgerDirection(accountTransactionEvent.transactionType()),
			accountTransactionEvent.amount(),
			balanceAfter,
			accountTransactionEvent.occurredAt()
		);

		ledgerEntryRepository.save(ledgerEntry);
	}

	public void saveAccountStatusHistory(String eventId, AccountStatusUpdatedEvent accountStatusUpdatedEvent) {
		final AccountStatusHistory accountStatusHistory = AccountStatusHistory.create(
			accountStatusUpdatedEvent.accountId(),
			accountStatusUpdatedEvent.memberId(),
			eventId,
			accountStatusUpdatedEvent.previousStatus(),
			accountStatusUpdatedEvent.newStatus(),
			accountStatusUpdatedEvent.changedAt()
		);

		statusHistoryRepository.save(accountStatusHistory);
	}

	private LedgerDirection resolveLedgerDirection(TransactionType transactionType) {
		return switch (transactionType) {
			case DEPOSIT, TRANSFER_IN -> LedgerDirection.CREDIT;
			case WITHDRAWAL, TRANSFER_OUT -> LedgerDirection.DEBIT;
		};
	}
}
