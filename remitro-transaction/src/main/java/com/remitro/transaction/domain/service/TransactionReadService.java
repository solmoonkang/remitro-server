package com.remitro.transaction.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.remitro.common.error.exception.NotFoundException;
import com.remitro.common.error.model.ErrorMessage;
import com.remitro.transaction.domain.model.Transaction;
import com.remitro.transaction.domain.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionReadService {

	private final TransactionRepository transactionRepository;

	public Transaction findTransactionById(Long transactionId) {
		return transactionRepository.findById(transactionId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.TRANSACTION_NOT_FOUND));
	}

	public List<Transaction> findTransactionsByAccountId(Long accountId) {
		return transactionRepository.findAllByAccountIdOrderByDateDesc(accountId);
	}
}
