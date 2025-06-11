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
import com.remitroserver.api.domain.transaction.entity.StatusLog;
import com.remitroserver.api.domain.transaction.entity.Transaction;
import com.remitroserver.api.domain.transaction.repository.StatusLogRepository;
import com.remitroserver.api.domain.transaction.repository.TransactionRepository;
import com.remitroserver.api.infrastructure.redis.ValueRedisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionWriteService {

	private final TransactionRepository transactionRepository;
	private final StatusLogRepository statusLogRepository;
	private final TransactionReadService transactionReadService;
	private final ValueRedisRepository valueRedisRepository;
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
	public void createTransactionWithLog(Account fromAccount, Account toAccount, Money amount, String idempotencyKey) {
		final Transaction transaction = Transaction.create(fromAccount, toAccount, amount);

		valueRedisRepository.save("IDEMPOTENCY_KEY:" + idempotencyKey, "1", Duration.ofMinutes(10));
		transactionRepository.save(transaction);
		statusLogRepository.save(StatusLog.create(transaction, COMPLETED));
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
