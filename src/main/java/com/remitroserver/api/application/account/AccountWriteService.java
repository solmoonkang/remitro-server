package com.remitroserver.api.application.account;

import static com.remitroserver.api.domain.account.model.AccountStatus.*;
import static com.remitroserver.global.error.model.ErrorMessage.*;

import java.util.UUID;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.remitroserver.api.domain.account.entity.Account;
import com.remitroserver.api.domain.account.model.Money;
import com.remitroserver.api.domain.account.repository.AccountRepository;
import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.api.dto.account.request.AccountCreateRequest;
import com.remitroserver.global.error.exception.ConflictException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountWriteService {

	private static final int MAX_RETRIES = 5;
	private static final long BACKOFF_MS = 50;

	private final AccountRepository accountRepository;
	private final AccountReadService accountReadService;

	public void createAccount(String accountNumber, Member member, AccountCreateRequest accountCreateRequest) {
		final Account account = Account.create(accountNumber, member, accountCreateRequest.accountType());
		accountRepository.save(account);
	}

	@Retryable(
		value = ObjectOptimisticLockingFailureException.class,
		maxAttempts = MAX_RETRIES,
		backoff = @Backoff(delay = BACKOFF_MS)
	)
	@Transactional(isolation = Isolation.READ_COMMITTED)
	protected void deposit(UUID accountToken, Member member, Long rawAmount) {
		final Account account = accountReadService.getAccountByTokenAndOwner(accountToken, member);
		final Money amount = Money.fromPositive(rawAmount);

		account.isOwner(member);
		account.deposit(amount);
	}

	@Retryable(
		value = ObjectOptimisticLockingFailureException.class,
		maxAttempts = MAX_RETRIES,
		backoff = @Backoff(delay = BACKOFF_MS)
	)
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void withdraw(UUID accountToken, Member member, Long rawAmount) {
		final Account account = accountReadService.getAccountByTokenAndOwner(accountToken, member);
		final Money amount = Money.fromPositive(rawAmount);

		account.isOwner(member);
		account.withdraw(amount);
	}

	public void updateSuspendAccount(UUID accountToken, Member member) {
		final Account account = accountReadService.getAccountByTokenAndOwner(accountToken, member);
		account.changeStatusTo(SUSPENDED);
	}

	public void updateActivateAccount(UUID accountToken, Member member) {
		final Account account = accountReadService.getAccountByTokenAndOwner(accountToken, member);
		account.changeStatusTo(ACTIVE);
	}

	public void updateCloseAccount(UUID accountToken, Member member) {
		final Account account = accountReadService.getAccountByTokenAndOwner(accountToken, member);
		account.changeStatusTo(CLOSED);
	}

	@Recover
	public void recover(
		ObjectOptimisticLockingFailureException lockingFailureException,
		UUID accountToken,
		Member member,
		Long rawAmount) {

		throw new ConflictException(ACCOUNT_CONCURRENCY_ERROR);
	}
}
