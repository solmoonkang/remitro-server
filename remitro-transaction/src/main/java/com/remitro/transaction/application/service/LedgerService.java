package com.remitro.transaction.application.service;

import org.springframework.stereotype.Service;

import com.remitro.transaction.domain.enums.LedgerDirection;
import com.remitro.transaction.domain.enums.TransactionType;
import com.remitro.transaction.domain.event.AccountTransactionEvent;
import com.remitro.transaction.domain.model.LedgerEntry;
import com.remitro.transaction.domain.model.Transaction;
import com.remitro.transaction.domain.repository.LedgerEntryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LedgerService {

	private final LedgerEntryRepository ledgerEntryRepository;

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

	private LedgerDirection resolveLedgerDirection(TransactionType transactionType) {
		return switch (transactionType) {
			case DEPOSIT, TRANSFER_IN -> LedgerDirection.CREDIT;
			case WITHDRAWAL, TRANSFER_OUT -> LedgerDirection.DEBIT;
		};
	}
}
