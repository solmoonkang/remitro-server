package com.remitro.account.application.command.account;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.model.Account;
import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.ForbiddenException;

@Component
public class AccountAccessValidator {

	public void validateOwnership(Account account, Long memberId) {
		if (!account.getMemberId().equals(memberId)) {
			throw new ForbiddenException(ErrorCode.ACCOUNT_OWNERSHIP_REQUIRED);
		}
	}
}
