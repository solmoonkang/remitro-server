package com.remitro.account.application.common.support;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.account.repository.AccountRepository;
import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.NotFoundException;
import com.remitro.common.error.message.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountFinder {

	private final AccountRepository accountRepository;

	public Account getById(Long accountId) {
		return accountRepository.findById(accountId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.ACCOUNT_NOT_FOUND, ErrorMessage.ACCOUNT_NOT_FOUND));
	}
}
