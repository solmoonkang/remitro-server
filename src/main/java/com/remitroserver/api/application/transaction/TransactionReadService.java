package com.remitroserver.api.application.transaction;

import static com.remitroserver.global.error.model.ErrorMessage.*;

import org.springframework.stereotype.Service;

import com.remitroserver.api.domain.transaction.repository.TransactionRepository;
import com.remitroserver.global.error.exception.ConflictException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionReadService {

	private final TransactionRepository transactionRepository;

	public void validateIdempotencyKeyExists(String idempotencyKey) {
		if (transactionRepository.existsByIdempotencyKey(idempotencyKey)) {
			throw new ConflictException(DUPLICATED_IDEMPOTENCY_KEY_ERROR);
		}
	}
}
