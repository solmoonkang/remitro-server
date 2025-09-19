package com.remitro.transaction.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.validator.AccountValidator;
import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.service.AccountReadService;
import com.remitro.common.auth.model.AuthMember;
import com.remitro.transaction.application.dto.response.TransactionDetailResponse;
import com.remitro.transaction.application.mapper.TransactionMapper;
import com.remitro.transaction.application.validator.TransactionValidator;
import com.remitro.transaction.domain.model.Transaction;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

	private final TransactionValidator transactionValidator;
	private final AccountValidator accountValidator;
	private final TransactionWriteService transactionWriteService;
	private final TransactionReadService transactionReadService;
	private final AccountReadService accountReadService;

	@Transactional(propagation = Propagation.MANDATORY)
	public void recordTransferTransaction(Account sender, Account receiver, Long amount, String idempotencyKey) {
		transactionValidator.validateIdempotencyKeyNotDuplicated(idempotencyKey);
		final Transaction outbound = Transaction.createSenderTransaction(sender, receiver, amount, idempotencyKey);
		final Transaction inbound = Transaction.createReceiverTransaction(sender, receiver, amount, idempotencyKey);
		transactionWriteService.saveTransferTransaction(outbound, inbound);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void recordDepositTransaction(Account receiver, Long amount, String idempotencyKey) {
		transactionValidator.validateIdempotencyKeyNotDuplicated(idempotencyKey);
		final Transaction deposit = Transaction.createDepositTransaction(receiver, amount, idempotencyKey);
		transactionWriteService.saveDepositTransaction(deposit);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void recordWithdrawalTransaction(Account sender, Long amount, String idempotencyKey) {
		transactionValidator.validateIdempotencyKeyNotDuplicated(idempotencyKey);
		final Transaction withdrawal = Transaction.createWithdrawalTransaction(sender, amount, idempotencyKey);
		transactionWriteService.saveWithdrawalTransaction(withdrawal);
	}

	public TransactionDetailResponse findTransactionDetail(AuthMember authMember, Long transactionId) {
		final Transaction transaction = transactionReadService.findTransactionById(transactionId);
		transactionValidator.validateTransactionAccess(authMember.id(), transaction);
		return TransactionMapper.toTransactionDetailResponse(transaction);
	}

	public List<TransactionDetailResponse> findAllTransactions(AuthMember authMember, Long accountId) {
		final Account account = accountReadService.findAccountById(accountId);
		accountValidator.validateAccountAccess(account.getMember().getId(), authMember.id());
		final List<Transaction> transactions = transactionReadService.findTransactionsByAccountId(accountId);
		return TransactionMapper.toTransactionListResponse(transactions);
	}
}
