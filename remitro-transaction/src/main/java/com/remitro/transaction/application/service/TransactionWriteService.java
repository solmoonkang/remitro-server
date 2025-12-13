package com.remitro.transaction.application.service;

import org.springframework.stereotype.Service;

import com.remitro.transaction.domain.event.AccountTransactionEvent;
import com.remitro.transaction.domain.model.Transaction;
import com.remitro.transaction.domain.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionWriteService {

	private final TransactionRepository transactionRepository;

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
}
