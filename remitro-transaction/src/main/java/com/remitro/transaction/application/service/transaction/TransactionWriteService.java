package com.remitro.transaction.application.service.transaction;

import org.springframework.stereotype.Service;

import com.remitro.transaction.domain.event.AccountTransactionEvent;
import com.remitro.transaction.domain.transaction.model.Transaction;
import com.remitro.transaction.domain.transaction.repository.TransactionRepository;

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
