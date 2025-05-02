package com.remitroserver.api.application.account;

import static com.remitroserver.global.error.model.ErrorMessage.*;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitroserver.api.application.account.mapper.AccountMapper;
import com.remitroserver.api.application.member.MemberReadService;
import com.remitroserver.api.application.transaction.TransactionReadService;
import com.remitroserver.api.application.transaction.mapper.TransactionMapper;
import com.remitroserver.api.domain.account.entity.Account;
import com.remitroserver.api.domain.auth.model.AuthMember;
import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.api.domain.transaction.entity.Transaction;
import com.remitroserver.api.dto.account.request.AccountAmountRequest;
import com.remitroserver.api.dto.account.request.AccountCreateRequest;
import com.remitroserver.api.dto.account.response.AccountBalanceResponse;
import com.remitroserver.api.dto.account.response.AccountDetailResponse;
import com.remitroserver.api.dto.account.response.AccountSummaryResponse;
import com.remitroserver.api.dto.transaction.response.TransactionSummaryResponse;
import com.remitroserver.global.error.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

	private final PasswordEncoder passwordEncoder;
	private final MemberReadService memberReadService;
	private final AccountReadService accountReadService;
	private final AccountWriteService accountWriteService;
	private final TransactionReadService transactionReadService;

	@Transactional
	public void createAccount(AuthMember authMember, AccountCreateRequest accountCreateRequest) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());
		accountReadService.validateAccountLimitExceeded(member, accountCreateRequest.accountType());

		validatePasswordConfirmationMatch(
			accountCreateRequest.accountPassword(), accountCreateRequest.accountCheckPassword());

		final String accountNumber = accountReadService.generateUniqueAccountNumber(accountCreateRequest.accountType());
		final String encodedAccountPassword = passwordEncoder.encode(accountCreateRequest.accountPassword());
		accountWriteService.createAccount(accountNumber, member, encodedAccountPassword, accountCreateRequest);
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

	public void depositToAccount(UUID accountToken, AuthMember authMember, AccountAmountRequest accountAmountRequest) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());
		accountWriteService.deposit(accountToken, member, accountAmountRequest.amount());
	}

	public void withdrawFromAccount(
		UUID accountToken,
		AuthMember authMember,
		AccountAmountRequest accountAmountRequest) {

		final Member member = memberReadService.getMemberByEmail(authMember.email());
		accountWriteService.withdraw(accountToken, member, accountAmountRequest.amount());
	}

	@Transactional
	public void suspendAccount(UUID accountToken, AuthMember authMember) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());
		accountWriteService.updateSuspendAccount(accountToken, member);
	}

	@Transactional
	public void activateAccount(UUID accountToken, AuthMember authMember) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());
		accountWriteService.updateActivateAccount(accountToken, member);
	}

	@Transactional
	public void closeAccount(UUID accountToken, AuthMember authMember) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());
		accountWriteService.updateCloseAccount(accountToken, member);
	}

	private void validatePasswordConfirmationMatch(String password, String checkPassword) {
		if (!password.equals(checkPassword)) {
			throw new BadRequestException(PASSWORD_MISMATCH_ERROR);
		}
	}
}
