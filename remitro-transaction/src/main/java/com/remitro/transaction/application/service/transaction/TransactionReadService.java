package com.remitro.transaction.application.service.transaction;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.remitro.transaction.domain.transaction.repository.LedgerEntryRepository;
import com.remitro.transaction.domain.transaction.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionReadService {

	private final TransactionRepository transactionRepository;
	private final LedgerEntryRepository ledgerEntryRepository;

	public boolean existsTransactionEvent(String eventId) {
		return transactionRepository.existsByEventId(eventId);
	}

	public Long findLatestBalance(Long accountId) {
		return ledgerEntryRepository.findLatestLedgerEntry(accountId, PageRequest.of(0, 1)).stream()
			.findFirst()
			.orElse(0L);
	}
}
