package com.remitro.account.domain.account.policy;

import org.springframework.stereotype.Component;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.ConflictException;
import com.remitro.common.error.message.ErrorMessage;

@Component
public class DepositAccountOpenPolicy {

	private static final int MAX_DEPOSIT_ACCOUNTS_PER_MEMBER = 5;

	public void validateOpen(int existingDepositAccountCount) {
		if (existingDepositAccountCount >= MAX_DEPOSIT_ACCOUNTS_PER_MEMBER) {
			throw new ConflictException(
				ErrorCode.ACCOUNT_LIMIT_EXCEEDED, ErrorMessage.ACCOUNT_LIMIT_EXCEEDED
			);
		}
	}
}
