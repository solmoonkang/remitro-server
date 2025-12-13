package com.remitro.account.application.service.idempotency;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.remitro.account.domain.enums.IdempotencyOperationType;
import com.remitro.account.domain.model.Idempotency;
import com.remitro.account.domain.repository.IdempotencyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

	private final IdempotencyRepository idempotencyRepository;

	public Idempotency acquireIdempotencyOrGet(
		String idempotencyKey,
		Long memberId,
		IdempotencyOperationType idempotencyOperationType
	) {
		try {
			return idempotencyRepository.save(
				Idempotency.createPending(idempotencyKey, memberId, idempotencyOperationType)
			);

		} catch (DataIntegrityViolationException e) {
			return idempotencyRepository.findById(idempotencyKey)
				.orElseThrow();
		}
	}
}
