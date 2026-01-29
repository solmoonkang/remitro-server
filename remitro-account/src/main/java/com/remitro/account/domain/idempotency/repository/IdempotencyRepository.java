package com.remitro.account.domain.idempotency.repository;

import com.remitro.account.domain.idempotency.model.Idempotency;

public interface IdempotencyRepository {

	boolean saveIfAbsent(Idempotency idempotency);

	void deleteByRequestId(String requestId);
}
