package com.remitro.account.application.validator;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.enums.AccountStatus;
import com.remitro.common.error.exception.ConflictException;
import com.remitro.common.error.model.ErrorMessage;

@Component
public class AccountStatusTransactionValidator {

	public void validateStatusTransition(AccountStatus currentStatus, AccountStatus targetStatus) {
		if (currentStatus == AccountStatus.TERMINATED) {
			throw new ConflictException(ErrorMessage.ACCOUNT_ALREADY_TERMINATED);
		}

		if (currentStatus == AccountStatus.DORMANT && targetStatus == AccountStatus.NORMAL) {
			throw new ConflictException(ErrorMessage.DORMANT_ACCOUNT_CANNOT_RESTORE);
		}

		if (currentStatus == AccountStatus.FROZEN && targetStatus == AccountStatus.DORMANT) {
			throw new ConflictException(ErrorMessage.ACCOUNT_FROZEN_CANNOT_DORMANT);
		}

		if (currentStatus == AccountStatus.SUSPENDED && targetStatus == AccountStatus.NORMAL) {
			throw new ConflictException(ErrorMessage.ACCOUNT_SUSPENDED_CANNOT_RESTORE);
		}

		if (currentStatus == targetStatus) {
			throw new ConflictException(ErrorMessage.ACCOUNT_STATUS_ALREADY_SAME);
		}
	}
}
