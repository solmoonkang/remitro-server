package com.remitro.account.application.common.support.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.remitro.account.domain.idempotency.enums.IdempotencyOperationType;
import com.remitro.account.domain.idempotency.repository.IdempotencyRepository;
import com.remitro.account.domain.idempotency.model.Idempotency;
import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.message.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdempotencyCommandService {

	private final IdempotencyRepository idempotencyRepository;

	public Idempotency acquireOrGet(
		Long memberId,
		String idempotencyKey,
		IdempotencyOperationType idempotencyOperationType
	) {
		try {
			return idempotencyRepository.save(
				Idempotency.createPending(idempotencyKey, memberId, idempotencyOperationType)
			);
		} catch (DataIntegrityViolationException e) {
			return idempotencyRepository.findById(idempotencyKey)
				.orElseThrow(() -> new BadRequestException(
					ErrorCode.IDEMPOTENCY_KEY_MISSING, ErrorMessage.IDEMPOTENCY_KEY_MISSING)
				);
		}
	}
}
