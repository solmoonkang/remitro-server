package com.remitro.account.application.service.transaction;

import org.springframework.stereotype.Service;

import com.remitro.account.application.dto.request.WithdrawFormRequest;
import com.remitro.account.application.service.AccountWriteService;
import com.remitro.account.application.validator.AccountValidator;
import com.remitro.account.domain.model.Account;
import com.remitro.account.application.service.AccountReadService;
import com.remitro.account.application.service.support.IdempotencyKeyHandler;
import com.remitro.account.infrastructure.redis.DistributedLockManager;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WithdrawTransactionHandler {

	private final AccountValidator accountValidator;
	private final DistributedLockManager distributedLockManager;
	private final AccountReadService accountReadService;
	private final AccountWriteService accountWriteService;
	private final IdempotencyKeyHandler idempotencyKeyHandler;

	public void withdrawToAccount(Long accountId, String idempotencyKey, WithdrawFormRequest withdrawFormRequest) {
		idempotencyKeyHandler.preventDuplicateRequestAndRecordKey(idempotencyKey);
		accountValidator.validateAmountPositive(withdrawFormRequest.amount());

		distributedLockManager.executeProtectedDistributedLock(accountId, () -> {
			final Account sender = accountReadService.findAccountById(accountId);
			accountValidator.validateAccountPasswordMatch(withdrawFormRequest.password(), sender.getPassword());
			accountValidator.validateSufficientBalance(sender.getBalance(), withdrawFormRequest.amount());
			accountWriteService.processWithdraw(sender, withdrawFormRequest);
		});
	}
}
