package com.remitro.account.infrastructure.aop;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.repository.TransactionLockRepository;
import com.remitro.web.aop.DistributedLockManager;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountLockManager implements DistributedLockManager {

	private final TransactionLockRepository transactionLockRepository;

	@Override
	public void acquireLock(Long targetId, long waitTime, long leaseTime) {
		transactionLockRepository.acquireLock(targetId, waitTime, leaseTime);
	}

	@Override
	public void releaseLock(Long targetId) {
		transactionLockRepository.releaseLock(targetId);
	}
}
