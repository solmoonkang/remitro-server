package com.remitro.account.application.read;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.account.repository.AccountRepository;
import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountFinder {

	private final AccountRepository accountRepository;

	public Account getAccountByIdWithLock(Long accountId) {
		return accountRepository.findByIdWithLock(accountId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
	}
}
