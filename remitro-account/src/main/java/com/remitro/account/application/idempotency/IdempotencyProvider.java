package com.remitro.account.application.idempotency;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.idempotency.model.Idempotency;
import com.remitro.account.domain.idempotency.repository.IdempotencyRepository;
import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class IdempotencyProvider {

	private final IdempotencyRepository idempotencyRepository;
	private final Clock clock;

	public void process(String requestId, Long memberId) {
		final boolean acquired = idempotencyRepository.saveIfAbsent(
			Idempotency.issue(requestId, memberId, LocalDateTime.now(clock))
		);

		if (!acquired) {
			throw new BadRequestException(ErrorCode.DUPLICATE_RESOURCE);
		}
	}
}
