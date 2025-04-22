package com.remitroserver.api.application.transaction;

import static com.remitroserver.global.error.model.ErrorMessage.*;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.api.domain.transaction.entity.Transaction;
import com.remitroserver.api.domain.transaction.model.TransactionStatus;
import com.remitroserver.api.domain.transaction.repository.TransactionRepository;
import com.remitroserver.global.error.exception.ConflictException;
import com.remitroserver.global.error.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionReadService {

	private final TransactionRepository transactionRepository;

	public Transaction getRequestedTransactionByTokenAndOwner(UUID transactionToken, Member member) {
		return transactionRepository
			.findByTransactionTokenAndFromAccountMemberAndStatus(transactionToken, member, TransactionStatus.REQUESTED)
			.orElseThrow(() -> new NotFoundException(TRANSACTION_NOT_FOUND_ERROR));
	}

	public void validateIdempotencyKeyExists(String idempotencyKey) {
		if (transactionRepository.existsByIdempotencyKey(idempotencyKey)) {
			throw new ConflictException(DUPLICATED_IDEMPOTENCY_KEY_ERROR);
		}
	}
}
