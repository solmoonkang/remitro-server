package com.remitro.transaction.application.service;

import org.springframework.stereotype.Service;

import com.remitro.transaction.domain.repository.StatusHistoryRepository;
import com.remitro.transaction.domain.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionReadService {

	private final TransactionRepository transactionRepository;
	private final StatusHistoryRepository statusHistoryRepository;

	public boolean existsTransactionEvent(String eventId) {
		return transactionRepository.existsByEventId(eventId);
	}

	public boolean existsStatusChangeEvent(String eventId) {
		return statusHistoryRepository.existsByEventId(eventId);
	}
}
