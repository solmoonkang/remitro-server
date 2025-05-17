package com.remitroserver.api.application.transaction;

import static com.remitroserver.api.domain.transaction.model.TransactionStatus.*;

import java.time.Duration;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitroserver.api.domain.account.entity.Account;
import com.remitroserver.api.domain.account.model.Money;
import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.api.domain.transaction.entity.StatusLog;
import com.remitroserver.api.domain.transaction.entity.Transaction;
import com.remitroserver.api.domain.transaction.repository.StatusLogRepository;
import com.remitroserver.api.domain.transaction.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionWriteService {

	private final TransactionRepository transactionRepository;
	private final StatusLogRepository statusLogRepository;
	private final TransactionReadService transactionReadService;

	@Transactional
	public void createTransactionWithLog(Account fromAccount, Account toAccount, Money amount, String idempotencyKey) {
		final Transaction transaction = Transaction.create(fromAccount, toAccount, amount, idempotencyKey);

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
