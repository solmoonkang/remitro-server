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

	public void withdrawToAccount(Long accountId, WithdrawFormRequest withdrawFormRequest) {
		accountValidator.validateAmountPositive(withdrawFormRequest.amount());

		distributedLockManager.executeProtectedDistributedLock(accountId, () -> {
			final Account sender = accountReadService.findAccountById(accountId);
			accountValidator.validateAccountPasswordMatch(withdrawFormRequest.password(), sender.getPassword());
			accountWriteService.processWithdraw(sender, withdrawFormRequest);
		});
	}
}
