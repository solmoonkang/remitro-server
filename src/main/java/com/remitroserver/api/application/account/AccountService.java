package com.remitroserver.api.application.account;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitroserver.api.application.account.mapper.AccountMapper;
import com.remitroserver.api.application.member.MemberReadService;
import com.remitroserver.api.domain.account.entity.Account;
import com.remitroserver.api.domain.account.repository.AccountRepository;
import com.remitroserver.api.domain.auth.model.AuthMember;
import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.api.dto.account.request.AccountCreateRequest;
import com.remitroserver.api.dto.account.response.AccountSummaryResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

	private final AccountRepository accountRepository;
	private final MemberReadService memberReadService;
	private final AccountReadService accountReadService;

	@Transactional
	public void createAccount(AuthMember authMember, AccountCreateRequest accountCreateRequest) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());
		accountReadService.validateAccountLimitExceeded(member, accountCreateRequest.accountType());

		final String accountNumber = accountReadService.generateUniqueAccountNumber(accountCreateRequest.accountType());
		final Account account = Account.create(accountNumber, member, accountCreateRequest.accountType());

		accountRepository.save(account);
	}

	public List<AccountSummaryResponse> getAllMyAccounts(AuthMember authMember) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());
		final List<Account> accounts = accountReadService.getAccountsByMember(member);

		return accounts.stream()
			.map(AccountMapper::toSummaryResponse)
			.toList();
	}
}
