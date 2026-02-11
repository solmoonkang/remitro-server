package com.remitro.account.domain.account.repository;

public interface TransactionLockRepository {

	void acquireLock(Long accountId, long waitTime, long leaseTime);

	void releaseLock(Long accountId);
}
