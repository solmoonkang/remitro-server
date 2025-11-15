package com.remitro.account.application.service.transaction;

import org.springframework.stereotype.Service;

import com.remitro.account.application.dto.request.DepositFormRequest;
import com.remitro.account.application.service.AccountWriteService;
import com.remitro.account.application.validator.AccountValidator;
import com.remitro.account.domain.model.Account;
import com.remitro.account.application.service.AccountReadService;
import com.remitro.account.application.service.support.IdempotencyKeyHandler;
import com.remitro.account.infrastructure.redis.DistributedLockManager;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepositTransactionHandler {

	private final AccountValidator accountValidator;
	private final DistributedLockManager distributedLockManager;
	private final AccountReadService accountReadService;
	private final AccountWriteService accountWriteService;
	private final IdempotencyKeyHandler idempotencyKeyHandler;

	public void depositToAccount(Long accountId, String idempotencyKey, DepositFormRequest depositFormRequest) {
		idempotencyKeyHandler.preventDuplicateRequestAndRecordKey(idempotencyKey);
		accountValidator.validateAmountPositive(depositFormRequest.amount());

		distributedLockManager.executeProtectedDistributedLock(accountId, () -> {
			final Account receiver = accountReadService.findAccountById(accountId);
			accountWriteService.processDeposit(receiver, depositFormRequest);
		});
	}
}
