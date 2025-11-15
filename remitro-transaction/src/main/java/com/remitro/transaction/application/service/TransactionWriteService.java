package com.remitro.transaction.application.service;

import org.springframework.stereotype.Service;

import com.remitro.transaction.domain.model.Transaction;
import com.remitro.transaction.domain.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionWriteService {

	private final TransactionRepository transactionRepository;

	public void saveTransferTransaction(Transaction senderTransaction, Transaction receiverTransaction) {
		transactionRepository.save(senderTransaction);
		transactionRepository.save(receiverTransaction);
	}

	public void saveDepositTransaction(Transaction depositTransaction) {
		transactionRepository.save(depositTransaction);
	}

	public void saveWithdrawalTransaction(Transaction drawalTransaction) {
		transactionRepository.save(drawalTransaction);
	}
}
