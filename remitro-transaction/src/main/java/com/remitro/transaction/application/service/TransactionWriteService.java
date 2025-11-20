package com.remitro.transaction.application.service;

import org.springframework.stereotype.Service;

import com.remitro.common.contract.account.AccountTransactionEvent;
import com.remitro.transaction.domain.model.LedgerEntry;
import com.remitro.transaction.domain.model.Transaction;
import com.remitro.transaction.domain.model.enums.LedgerDirection;
import com.remitro.transaction.domain.model.enums.TransactionType;
import com.remitro.transaction.domain.repository.LedgerEntryRepository;
import com.remitro.transaction.domain.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionWriteService {

	private final TransactionRepository transactionRepository;
	private final LedgerEntryRepository ledgerEntryRepository;

	public Transaction saveTransaction(
		String eventId,
		TransactionType transactionType,
		AccountTransactionEvent accountTransactionEvent
	) {
		final Transaction transaction = Transaction.create(
			eventId,
			transactionType,
			accountTransactionEvent.amount(),
			accountTransactionEvent.description(),
			accountTransactionEvent.occurredAt()
		);
		return transactionRepository.save(transaction);
	}

	public void saveLedgerEntry(
		Transaction transaction,
		LedgerDirection ledgerDirection,
		Long balanceAfter,
		AccountTransactionEvent accountTransactionEvent
	) {
		final LedgerEntry ledgerEntry = LedgerEntry.create(
			transaction,
			ledgerDirection,
			accountTransactionEvent.amount(),
			balanceAfter,
			accountTransactionEvent.occurredAt()
		);
		ledgerEntryRepository.save(ledgerEntry);
	}
}
