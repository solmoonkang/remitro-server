package com.remitro.account.domain.service;

import org.springframework.stereotype.Service;

import com.remitro.account.application.dto.request.DepositFormRequest;
import com.remitro.account.application.validator.AccountValidator;
import com.remitro.account.domain.model.Account;
import com.remitro.account.infrastructure.redis.DistributedLockManager;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountDepositService {

	private final AccountValidator accountValidator;
	private final DistributedLockManager distributedLockManager;
	private final AccountReadService accountReadService;
	private final AccountWriteService accountWriteService;

	public void depositToAccount(Long accountId, DepositFormRequest depositFormRequest) {
		accountValidator.validateAmountPositive(depositFormRequest.amount());

		distributedLockManager.executeProtectedDistributedLock(accountId, () -> {
			final Account receiver = accountReadService.findAccountById(accountId);
			accountWriteService.processDeposit(receiver, depositFormRequest);
		});
	}
}
