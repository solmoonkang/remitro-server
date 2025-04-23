package com.remitroserver.api.application.account;

import static com.remitroserver.api.domain.account.model.AccountStatus.*;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitroserver.api.application.account.mapper.AccountMapper;
import com.remitroserver.api.application.member.MemberReadService;
import com.remitroserver.api.application.transaction.TransactionReadService;
import com.remitroserver.api.application.transaction.mapper.TransactionMapper;
import com.remitroserver.api.domain.account.entity.Account;
import com.remitroserver.api.domain.account.repository.AccountRepository;
import com.remitroserver.api.domain.auth.model.AuthMember;
import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.api.domain.transaction.entity.Transaction;
import com.remitroserver.api.dto.account.request.AccountCreateRequest;
import com.remitroserver.api.dto.account.response.AccountBalanceResponse;
import com.remitroserver.api.dto.account.response.AccountDetailResponse;
import com.remitroserver.api.dto.account.response.AccountSummaryResponse;
import com.remitroserver.api.dto.transaction.response.TransactionSummaryResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

	private final AccountRepository accountRepository;
	private final MemberReadService memberReadService;
	private final AccountReadService accountReadService;
	private final TransactionReadService transactionReadService;

	@Transactional
	public void createAccount(AuthMember authMember, AccountCreateRequest accountCreateRequest) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());
		accountReadService.validateAccountLimitExceeded(member, accountCreateRequest.accountType());

		final String accountNumber = accountReadService.generateUniqueAccountNumber(accountCreateRequest.accountType());
		final Account account = Account.create(accountNumber, member, accountCreateRequest.accountType());

		accountRepository.save(account);
	}

	public List<AccountSummaryResponse> findAllMyAccounts(AuthMember authMember) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());
		final List<Account> accounts = accountReadService.getAccountsByMember(member);

		return accounts.stream()
			.map(AccountMapper::toSummaryResponse)
			.toList();
	}

	public AccountDetailResponse findAccountDetail(UUID accountToken, AuthMember authMember) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());
		final Account account = accountReadService.getAccountByTokenAndOwner(accountToken, member);

		final List<Transaction> recentTransactions = transactionReadService.getRecentTransactions(account);

		final List<TransactionSummaryResponse> transactionSummaryResponses = recentTransactions.stream()
			.map(TransactionMapper::toSummaryResponse)
			.toList();

		return AccountMapper.toDetailResponse(account, transactionSummaryResponses);
	}

	public AccountBalanceResponse findAccountBalance(UUID accountToken, AuthMember authMember) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());
		final Account account = accountReadService.getAccountByTokenAndOwner(accountToken, member);

		return AccountMapper.toBalanceResponse(account);
	}

	@Transactional
	public void suspendAccount(UUID accountToken, AuthMember authMember) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());
		final Account account = accountReadService.getAccountByTokenAndOwner(accountToken, member);

		account.changeStatusTo(SUSPENDED);
	}

	@Transactional
	public void activateAccount(UUID accountToken, AuthMember authMember) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());
		final Account account = accountReadService.getAccountByTokenAndOwner(accountToken, member);

		account.changeStatusTo(ACTIVE);
	}

	@Transactional
	public void closeAccount(UUID accountToken, AuthMember authMember) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());
		final Account account = accountReadService.getAccountByTokenAndOwner(accountToken, member);

		account.changeStatusTo(CLOSED);
	}
}
