package com.remitro.account.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.dto.request.OpenAccountRequest;
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
}
