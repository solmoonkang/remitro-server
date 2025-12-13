package com.remitro.transaction.application.validator;

import org.springframework.stereotype.Component;

import com.remitro.common.error.exception.ConflictException;
import com.remitro.common.error.model.ErrorMessage;
import com.remitro.transaction.domain.model.AccountStatusHistory;

@Component
public class AccountStatusHistoryValidator {

	public void validateInitialStatus(AccountStatusHistory accountStatusHistory) {
		if (accountStatusHistory.getPreviousStatus() != null) {
			throw new ConflictException(ErrorMessage.ACCOUNT_STATUS_HISTORY_PREVIOUS_MUST_BE_NULL);
		}
	}

	public void validateStatusUpdate(AccountStatusHistory accountStatusHistory) {
		if (accountStatusHistory.getPreviousStatus() == null) {
			throw new ConflictException(ErrorMessage.ACCOUNT_STATUS_HISTORY_PREVIOUS_REQUIRED);
		}

		if (accountStatusHistory.getPreviousStatus().equals(accountStatusHistory.getNewStatus())) {
			throw new ConflictException(ErrorMessage.ACCOUNT_STATUS_HISTORY_DUPLICATED);
		}
	}
}
