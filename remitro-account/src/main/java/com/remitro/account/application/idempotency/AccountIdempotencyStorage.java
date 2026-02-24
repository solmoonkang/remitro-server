package com.remitro.account.application.idempotency;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.idempotency.model.Idempotency;
import com.remitro.account.domain.idempotency.repository.IdempotencyRepository;
import com.remitro.web.util.idempotency.IdempotencyStorage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountIdempotencyStorage implements IdempotencyStorage {

	private final IdempotencyRepository idempotencyRepository;
	private final Clock clock;

	@Override
	public boolean saveIfAbsent(String requestId, Long memberId) {
		return idempotencyRepository.saveIfAbsent(
			Idempotency.issue(requestId, memberId, LocalDateTime.now(clock))
		);
	}
}
