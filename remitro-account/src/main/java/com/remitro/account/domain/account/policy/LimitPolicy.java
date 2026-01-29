package com.remitro.account.domain.account.policy;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.enums.AccountType;
import com.remitro.account.domain.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LimitPolicy {

	private final AccountRepository accountRepository;

	public boolean isExceeded(Long memberId, AccountType accountType) {
		int currentCount = accountRepository.countByMemberIdAndAccountType(memberId, accountType);
		return currentCount >= accountType.getMaxAccountsPerMember();
	}
}
