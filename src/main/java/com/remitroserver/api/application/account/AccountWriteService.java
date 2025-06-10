package com.remitroserver.api.application.account;

import static com.remitroserver.api.domain.account.model.AccountStatus.*;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitroserver.api.domain.account.entity.Account;
import com.remitroserver.api.domain.account.model.Money;
import com.remitroserver.api.domain.account.repository.AccountRepository;
import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.api.dto.account.request.AccountCreateRequest;
import com.remitroserver.global.common.util.PasswordValidator;
import com.remitroserver.global.error.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountWriteService {

	private final PasswordEncoder passwordEncoder;
	private final PasswordValidator passwordValidator;
	private final AccountRepository accountRepository;
	private final AccountReadService accountReadService;
	private final AccountLockoutService accountLockoutService;

	public void createAccount(String accountNumber, Member member, AccountCreateRequest accountCreateRequest) {
		final String encodedAccountPassword = passwordEncoder.encode(accountCreateRequest.accountPassword());
		final Account account = Account.create(
			accountNumber, member, encodedAccountPassword, accountCreateRequest.accountType());

		accountRepository.save(account);
	}

	@Transactional
	public void deposit(UUID accountToken, Member member, Long rawAmount) {
		final Account account = accountReadService.getAccountByTokenAndOwner(accountToken, member);
		account.deposit(Money.fromPositive(rawAmount));
	}

	@Transactional
	public void withdraw(UUID accountToken, Member member, Long rawAmount, String rawPassword) {
		final Account account = accountReadService.getAccountByTokenAndOwner(accountToken, member);
		accountLockoutService.validateAccountNotLocked(account.getId());

		try {
			passwordValidator.validatePasswordMatches(rawPassword, account.getPassword());
		} catch (BadRequestException e) {
			accountLockoutService.handlePasswordFailure(account.getId());
		}

		accountLockoutService.resetFailures(account.getId());
		account.withdraw(Money.fromPositive(rawAmount));
	}

	public void suspendAccount(UUID accountToken, Member member) {
		final Account account = accountReadService.getAccountByTokenAndOwner(accountToken, member);
		account.changeStatusTo(SUSPENDED);
	}

	public void activateAccount(UUID accountToken, Member member) {
		final Account account = accountReadService.getAccountByTokenAndOwner(accountToken, member);
		account.changeStatusTo(ACTIVE);
	}

	public void closeAccount(UUID accountToken, Member member) {
		final Account account = accountReadService.getAccountByTokenAndOwner(accountToken, member);
		account.changeStatusTo(CLOSED);
	}
}
