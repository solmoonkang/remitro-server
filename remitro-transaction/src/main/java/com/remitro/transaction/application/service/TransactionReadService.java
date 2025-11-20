package com.remitro.transaction.application.service;

import org.springframework.stereotype.Service;

import com.remitro.transaction.domain.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionReadService {

	private final TransactionRepository transactionRepository;

	public boolean existsByEventId(String eventId) {
		return transactionRepository.existsByEventId(eventId);
	}
}
