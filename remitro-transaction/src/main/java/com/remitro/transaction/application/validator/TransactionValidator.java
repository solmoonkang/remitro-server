package com.remitro.transaction.application.validator;

import org.springframework.stereotype.Component;

import com.remitro.common.error.exception.ConflictException;
import com.remitro.common.error.exception.ForbiddenException;
import com.remitro.common.error.model.ErrorMessage;
import com.remitro.transaction.domain.model.Transaction;
import com.remitro.transaction.domain.service.TransactionReadService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransactionValidator {

	private final TransactionReadService transactionReadService;

	public void validateTransactionAccess(Long loginMemberId, Transaction transaction) {
		if (!transaction.isSentBy(loginMemberId) && !transaction.isReceivedBy(loginMemberId)) {
			throw new ForbiddenException(ErrorMessage.TRANSACTION_ACCESS_FORBIDDEN);
		}
	}

	public void validateIdempotencyKeyNotDuplicated(String idempotencyKey) {
		if (transactionReadService.existsByIdempotencyKey(idempotencyKey)) {
			throw new ConflictException(ErrorMessage.IDEMPOTENCY_KEY_DUPLICATED);
		}
	}
}
