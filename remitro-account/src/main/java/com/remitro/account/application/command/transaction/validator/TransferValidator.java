package com.remitro.account.application.command.transaction.validator;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.account.policy.TransferPolicy;
import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.BadRequestException;
import com.remitro.support.exception.ForbiddenException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransferValidator {

	private final TransferPolicy transferPolicy;

	public void validateTransfer(Account fromAccount, Account toAccount, Long amoun) {
		if (transferPolicy.isSameAccount(fromAccount.getId(), toAccount.getId())) {
			throw new BadRequestException(ErrorCode.SAME_ACCOUNT_TRANSFER_NOT_ALLOWED);
		}

		if (transferPolicy.isNotTransferable(fromAccount)) {
			throw new ForbiddenException(ErrorCode.ACCOUNT_INACTIVE);
		}

		if (transferPolicy.isInsufficientBalance(fromAccount, amoun)) {
			throw new BadRequestException(ErrorCode.INSUFFICIENT_BALANCE);
		}

		if (transferPolicy.isNotTransferable(toAccount)) {
			throw new BadRequestException(ErrorCode.RECEIVER_ACCOUNT_INACTIVE);
		}
	}
}
