package com.remitro.account.application.usecase.query.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.common.support.AccountFinder;
import com.remitro.account.application.common.validator.AccountAccessValidator;
import com.remitro.account.application.usecase.query.dto.response.AccountDetailResponse;
import com.remitro.account.application.usecase.query.dto.response.AccountSummaryResponse;
import com.remitro.account.application.usecase.query.mapper.AccountQueryMapper;
import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountQueryService {

	private final AccountAccessValidator accountAccessValidator;
	private final AccountFinder accountFinder;
	private final AccountRepository accountRepository;
	private final Clock clock;

	public List<AccountSummaryResponse> getMyAllAccount(Long memberId) {
		return accountRepository.findAllByMemberId(memberId).stream()
			.map(AccountQueryMapper::toAccountSummaryResponse)
			.toList();
	}

	public AccountDetailResponse getMyAccount(Long memberId, Long accountId) {
		final Account account = accountFinder.getById(accountId);
		accountAccessValidator.validateOwner(account, memberId);
		return AccountQueryMapper.toAccountDetailResponse(account, LocalDateTime.now(clock));
	}
}
