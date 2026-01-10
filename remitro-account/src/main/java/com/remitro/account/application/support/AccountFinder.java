package com.remitro.account.application.support;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.account.repository.AccountQueryRepository;
import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.NotFoundException;
import com.remitro.common.error.message.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountFinder {

	private final AccountQueryRepository accountQueryRepository;

	public Account getById(Long accountId) {
		return accountQueryRepository.findById(accountId)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.ACCOUNT_NOT_FOUND, ErrorMessage.ACCOUNT_NOT_FOUND
			));
	}
}
