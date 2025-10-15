package com.remitro.account.domain.service;

import org.springframework.stereotype.Service;

import com.remitro.account.application.dto.request.WithdrawFormRequest;
import com.remitro.account.application.validator.AccountValidator;
import com.remitro.account.domain.model.Account;
import com.remitro.account.infrastructure.redis.DistributedLockManager;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountWithdrawService {

	private final AccountValidator accountValidator;
	private final DistributedLockManager distributedLockManager;
	private final AccountReadService accountReadService;
	private final AccountWriteService accountWriteService;
	private final IdempotencyService idempotencyService;

	public void withdrawToAccount(Long accountId, String idempotencyKey, WithdrawFormRequest withdrawFormRequest) {
		idempotencyService.preventDuplicateRequestAndRecordKey(idempotencyKey);
		accountValidator.validateAmountPositive(withdrawFormRequest.amount());

		distributedLockManager.executeProtectedDistributedLock(accountId, () -> {
			final Account sender = accountReadService.findAccountById(accountId);
			accountValidator.validateAccountPasswordMatch(withdrawFormRequest.password(), sender.getPassword());
			accountValidator.validateSufficientBalance(sender.getBalance(), withdrawFormRequest.amount());
			accountWriteService.processWithdraw(sender, withdrawFormRequest);
		});
	}
}
