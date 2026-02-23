package com.remitro.web.util.idempotency;

public interface IdempotencyStorage {

	boolean saveIfAbsent(String requestId, Long memberId);
}
