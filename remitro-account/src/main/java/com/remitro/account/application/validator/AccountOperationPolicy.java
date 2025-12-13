package com.remitro.account.application.validator;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.enums.AccountStatus;
import com.remitro.account.domain.model.Account;
import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.model.ErrorMessage;

@Component
public class AccountOperationPolicy {

	public void validateDepositAllowed(Account account) {
		AccountStatus accountStatus = account.getAccountStatus();

		if (accountStatus == AccountStatus.TERMINATED || accountStatus == AccountStatus.PENDING_TERMINATION) {
			throw new BadRequestException(ErrorMessage.ACCOUNT_ALREADY_TERMINATED);
		}

		if (accountStatus == AccountStatus.SUSPENDED) {
			throw new BadRequestException(ErrorMessage.ACCOUNT_SUSPENDED_CANNOT_RESTORE);
		}
	}

	public void validateWithdrawAllowed(Account account) {
		if (account.getAccountStatus() != AccountStatus.NORMAL) {
			throw new BadRequestException(ErrorMessage.ACCOUNT_CANNOT_WITHDRAW);
		}
	}

	public void validateTransferAllowed(Account account) {
		if (account.getAccountStatus() != AccountStatus.NORMAL) {
			throw new BadRequestException(ErrorMessage.ACCOUNT_CANNOT_TRANSFER);
		}
	}
}
