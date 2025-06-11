package com.remitroserver.api.application.transaction;

import static com.remitroserver.api.domain.transaction.model.TransactionStatus.*;

import java.time.Duration;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitroserver.api.application.account.AccountReadService;
import com.remitroserver.api.application.account.AccountWriteService;
import com.remitroserver.api.domain.account.entity.Account;
import com.remitroserver.api.domain.account.model.Money;
import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.api.domain.statusLog.entity.StatusLog;
import com.remitroserver.api.domain.statusLog.repository.StatusLogRepository;
import com.remitroserver.api.domain.transaction.entity.Transaction;
import com.remitroserver.api.domain.transaction.repository.IdempotencyKeyRedisRepository;
import com.remitroserver.api.domain.transaction.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionWriteService {

	private final TransactionRepository transactionRepository;
	private final StatusLogRepository statusLogRepository;
	private final IdempotencyKeyRedisRepository idempotencyKeyRedisRepository;
	private final TransactionReadService transactionReadService;
	private final AccountReadService accountReadService;
	private final AccountWriteService accountWriteService;

	@Transactional
	public void processDeposit(UUID accountToken, Member member, Long rawAmount) {
		final Account toAccount = accountReadService.getAccountByTokenAndOwner(accountToken, member);
		final Money amount = Money.fromPositive(rawAmount);

		final Transaction transaction = Transaction.createDeposit(toAccount, amount);
		transactionRepository.save(transaction);
		statusLogRepository.save(StatusLog.create(transaction, REQUESTED));

		accountWriteService.deposit(toAccount.getAccountToken(), member, rawAmount);
		statusLogRepository.save(StatusLog.create(transaction, COMPLETED));

		transaction.complete();
	}

	@Transactional
	public void processWithdraw(UUID accountToken, Member member, Long rawAmount, String rawPassword) {
		final Account fromAccount = accountReadService.getAccountByTokenAndOwner(accountToken, member);
		final Money amount = Money.fromPositive(rawAmount);

		final Transaction transaction = Transaction.createWithdraw(fromAccount, amount);
		transactionRepository.save(transaction);
		statusLogRepository.save(StatusLog.create(transaction, REQUESTED));

		accountWriteService.withdraw(accountToken, member, rawAmount, rawPassword);
		statusLogRepository.save(StatusLog.create(transaction, COMPLETED));

		transaction.complete();
	}

	@Transactional
	public Transaction createRequestedTransactionWithLog(Account fromAccount, Account toAccount, Money amount) {
		final Transaction transaction = Transaction.create(fromAccount, toAccount, amount);

		transactionRepository.save(transaction);
		statusLogRepository.save(StatusLog.create(transaction, COMPLETED));

		return transaction;
	}

	public void createTransactionIdempotencyKey(String idempotencyKey) {
		idempotencyKeyRedisRepository.saveIdempotencyKey(idempotencyKey);
	}

	public void storeTransactionTokenMapping(UUID transactionToken, String idempotencyKey) {
		idempotencyKeyRedisRepository.saveTransactionTokenMapping(transactionToken, idempotencyKey);
	}

	public void completeTransactionWithLog(UUID transactionToken, Member member) {
		final Transaction transaction = transactionReadService
			.getRequestedTransactionByTokenAndOwner(transactionToken, member);

		transaction.validateNotExpired(Duration.ofMinutes(5));
		transaction.complete();

		performTransfer(transaction.getFromAccount(), transaction.getToAccount(), transaction.getAmount());

		statusLogRepository.save(StatusLog.create(transaction, COMPLETED));
	}

	public void cancelTransactionWithLog(UUID transactionToken, Member member) {
		final Transaction transaction = transactionReadService
			.getRequestedTransactionByTokenAndOwner(transactionToken, member);

		transaction.cancel();

		statusLogRepository.save(StatusLog.create(transaction, CANCELLED));
	}

	private void performTransfer(Account fromAccount, Account toAccount, Money amount) {
		fromAccount.withdraw(amount);
		toAccount.deposit(amount);
	}
}
