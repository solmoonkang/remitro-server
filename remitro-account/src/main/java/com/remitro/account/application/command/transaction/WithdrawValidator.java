package com.remitro.account.application.command.transaction;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.account.policy.WithdrawPolicy;
import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.BadRequestException;
import com.remitro.support.exception.ForbiddenException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WithdrawValidator {

	private final WithdrawPolicy withdrawPolicy;

	public void validateWithdraw(Account account, Long amount) {
		if (withdrawPolicy.isInvalidAmount(amount)) {
			throw new BadRequestException(ErrorCode.INVALID_TRANSACTION_AMOUNT);
		}

		if (withdrawPolicy.isNotWithdrawable(account)) {
			throw new ForbiddenException(ErrorCode.ACCOUNT_INACTIVE);
		}

		if (withdrawPolicy.isInsufficientBalance(account, amount)) {
			throw new BadRequestException(ErrorCode.INSUFFICIENT_BALANCE);
		}
	}
}
