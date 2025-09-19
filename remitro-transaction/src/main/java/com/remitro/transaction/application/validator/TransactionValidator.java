package com.remitro.transaction.application.validator;

import org.springframework.stereotype.Component;

import com.remitro.common.error.exception.ForbiddenException;
import com.remitro.common.error.model.ErrorMessage;
import com.remitro.transaction.domain.model.Transaction;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransactionValidator {

	public void validateTransactionAccess(Long loginMemberId, Transaction transaction) {
		if (!transaction.isSentBy(loginMemberId) && !transaction.isReceivedBy(loginMemberId)) {
			throw new ForbiddenException(ErrorMessage.TRANSACTION_ACCESS_FORBIDDEN);
		}
	}
}
