package com.remitro.account.domain.account.policy;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LimitPolicy {

	private static final int MAX_ACCOUNT_COUNT = 5;

	private final AccountRepository accountRepository;

	public boolean isExceeded(Long memberId) {
		return accountRepository.countByMemberId(memberId) >= MAX_ACCOUNT_COUNT;
	}
}
