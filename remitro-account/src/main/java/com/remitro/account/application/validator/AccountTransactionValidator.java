package com.remitro.account.application.validator;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.model.Account;
import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.exception.ForbiddenException;
import com.remitro.common.error.model.ErrorMessage;

@Component
public class AccountTransactionValidator {

	public void validatePositiveAmount(Long amount) {
		if (amount == null || amount <= 0) {
			throw new BadRequestException(ErrorMessage.INVALID_AMOUNT);
		}
	}

	public void validateSufficientBalance(Account account, Long amount) {
		if (account.getBalance() < amount) {
			throw new BadRequestException(ErrorMessage.INSUFFICIENT_FUNDS);
		}
	}

	public void validateAccountOwner(Long accountOwnerId, Long loginMemberId) {
		if (!Objects.equals(accountOwnerId, loginMemberId)) {
			throw new ForbiddenException(ErrorMessage.ACCOUNT_ACCESS_FORBIDDEN);
		}
	}
}
