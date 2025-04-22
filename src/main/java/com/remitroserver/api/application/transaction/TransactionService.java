package com.remitroserver.api.application.transaction;

import static com.remitroserver.api.domain.transaction.model.TransactionStatus.*;
import static com.remitroserver.global.error.model.ErrorMessage.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitroserver.api.application.account.AccountReadService;
import com.remitroserver.api.application.member.MemberReadService;
import com.remitroserver.api.domain.account.entity.Account;
import com.remitroserver.api.domain.account.model.Money;
import com.remitroserver.api.domain.auth.model.AuthMember;
import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.api.domain.transaction.entity.Transaction;
import com.remitroserver.api.domain.transaction.entity.TransactionStatusLog;
import com.remitroserver.api.domain.transaction.repository.TransactionRepository;
import com.remitroserver.api.domain.transaction.repository.TransactionStatusLogRepository;
import com.remitroserver.api.dto.transaction.request.TransferRequest;
import com.remitroserver.global.error.exception.ConflictException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final TransactionStatusLogRepository transactionStatusLogRepository;
	private final MemberReadService memberReadService;
	private final AccountReadService accountReadService;

	@Transactional
	public void transferFunds(AuthMember authMember, TransferRequest transferRequest) {
		validateIdempotencyKeyExists(transferRequest.idempotencyKey());

		final Member member = memberReadService.getMemberByEmail(authMember.email());
		final Account fromAccount = accountReadService.getActiveAccountByTokenAndOwner(
			transferRequest.fromAccountToken(), member);
		final Account toAccount = accountReadService.getActiveAccountByAccountNumber(
			transferRequest.toAccountNumber());

		final Money amount = new Money(transferRequest.amount());
		final Transaction transaction = Transaction.create(fromAccount, toAccount, amount,
			transferRequest.idempotencyKey());

		performTransfer(fromAccount, toAccount, amount);

		transaction.complete();
		transactionRepository.save(transaction);
		transactionStatusLogRepository.save(TransactionStatusLog.create(transaction, COMPLETED));
	}

	private void performTransfer(Account fromAccount, Account toAccount, Money amount) {
		fromAccount.withdraw(amount);
		toAccount.deposit(amount);
	}

	private void validateIdempotencyKeyExists(String idempotencyKey) {
		if (transactionRepository.existsByIdempotencyKey(idempotencyKey)) {
			throw new ConflictException(DUPLICATED_IDEMPOTENCY_KEY_ERROR);
		}
	}
}
