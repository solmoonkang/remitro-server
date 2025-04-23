package com.remitroserver.api.application.account;

import static com.remitroserver.api.domain.account.model.AccountStatus.*;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.remitroserver.api.domain.account.entity.Account;
import com.remitroserver.api.domain.account.model.Money;
import com.remitroserver.api.domain.account.repository.AccountRepository;
import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.api.dto.account.request.AccountAmountRequest;
import com.remitroserver.api.dto.account.request.AccountCreateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountWriteService {

	private final AccountRepository accountRepository;
	private final AccountReadService accountReadService;

	public void createAccount(String accountNumber, Member member, AccountCreateRequest accountCreateRequest) {
		final Account account = Account.create(accountNumber, member, accountCreateRequest.accountType());
		accountRepository.save(account);
	}

	public void deposit(UUID accountToken, Member member, AccountAmountRequest accountAmountRequest) {
		final Account account = accountReadService.getAccountByTokenAndOwner(accountToken, member);
		final Money amount = Money.fromPositive(accountAmountRequest.amount());

		account.deposit(amount);
	}

	public void withdraw(UUID accountToken, Member member, AccountAmountRequest accountAmountRequest) {
		final Account account = accountReadService.getAccountByTokenAndOwner(accountToken, member);
		final Money amount = Money.fromPositive(accountAmountRequest.amount());

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
}
