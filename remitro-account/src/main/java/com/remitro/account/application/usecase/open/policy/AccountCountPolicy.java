package com.remitro.account.application.usecase.open.policy;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.repository.AccountRepository;
import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.ConflictException;
import com.remitro.common.error.message.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountCountPolicy {

	private static final int MAX_ACCOUNT_PER_MEMBER = 10;

	private final AccountRepository accountRepository;

	public void validateMaxAccountCount(Long memberId) {
		final long count = accountRepository.countByMemberId(memberId);

		if (count >= MAX_ACCOUNT_PER_MEMBER) {
			throw new ConflictException(
				ErrorCode.ACCOUNT_LIMIT_EXCEEDED, ErrorMessage.ACCOUNT_LIMIT_EXCEEDED
			);
		}
	}
}
