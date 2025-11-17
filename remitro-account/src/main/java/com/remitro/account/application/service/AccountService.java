package com.remitro.account.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.dto.request.OpenAccountRequest;
import com.remitro.account.application.dto.response.AccountDetailResponse;
import com.remitro.account.application.dto.response.AccountsSummaryResponse;
import com.remitro.account.application.dto.response.OpenAccountCreationResponse;
import com.remitro.account.application.mapper.AccountMapper;
import com.remitro.account.application.validator.AccountValidator;
import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.MemberProjection;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

	private final AccountValidator accountValidator;
	private final AccountReadService accountReadService;
	private final AccountWriteService accountWriteService;
	private final OpenAccountIdempotencyService openAccountIdempotencyService;

	@Transactional
	public OpenAccountCreationResponse openAccount(
		Long memberId,
		String idempotencyKey,
		OpenAccountRequest openAccountRequest) {

		openAccountIdempotencyService.validateOpenAccountIdempotency(memberId, idempotencyKey);

		final MemberProjection member = accountReadService.findMemberProjectionById(memberId);
		accountValidator.validateMemberIsActive(member);

		final Account account = accountWriteService.saveAccount(member, openAccountRequest);
		accountWriteService.appendAccountOpenedEventOutbox(account);

		return AccountMapper.toOpenAccountCreationResponse(account);
	}

	public AccountDetailResponse findAccountDetail(Long memberId, Long accountId) {
		final Account account = accountReadService.findAccountByIdAndMemberId(memberId, accountId);
		return AccountMapper.toAccountDetailResponse(account);
	}

	public AccountsSummaryResponse findMemberAccounts(Long memberId) {
		final MemberProjection member = accountReadService.findMemberProjectionById(memberId);
		final List<Account> accounts = accountReadService.findAllAccountByMemberId(memberId);
		return AccountMapper.toAccountsSummaryResponse(member, accounts);
	}
}
