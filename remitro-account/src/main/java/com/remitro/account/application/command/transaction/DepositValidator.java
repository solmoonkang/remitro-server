package com.remitro.account.application.command.transaction;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.account.policy.DepositPolicy;
import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.BadRequestException;
import com.remitro.support.exception.ForbiddenException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DepositValidator {

	private final DepositPolicy depositPolicy;

	public void validateDeposit(Account account, Long amount) {
		if (depositPolicy.isInvalidAmount(amount)) {
			throw new BadRequestException(ErrorCode.INVALID_TRANSACTION_AMOUNT);
		}

		if (depositPolicy.isNotDepositable(account)) {
			throw new ForbiddenException(ErrorCode.ACCOUNT_INACTIVE);
		}
	}
}
