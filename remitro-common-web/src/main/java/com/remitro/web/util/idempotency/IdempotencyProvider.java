package com.remitro.web.util.idempotency;

import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IdempotencyProvider {

	private final IdempotencyStorage idempotencyStorage;

	public void process(String requestId, Long memberId) {
		if (!idempotencyStorage.saveIfAbsent(requestId, memberId)) {
			throw new BadRequestException(ErrorCode.DUPLICATE_RESOURCE);
		}
	}
}
