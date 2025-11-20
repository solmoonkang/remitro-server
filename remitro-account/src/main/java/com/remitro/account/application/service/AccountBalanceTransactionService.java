package com.remitro.account.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.dto.request.deposit.DepositCommand;
import com.remitro.account.application.dto.response.DepositResponse;
import com.remitro.account.application.mapper.AccountMapper;
import com.remitro.account.application.validator.AccountValidator;
import com.remitro.account.domain.model.Account;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountBalanceTransactionService {

	private final AccountValidator accountValidator;
	private AccountReadService accountReadService;
	private AccountWriteService accountWriteService;
	private AccountOutboxService accountOutboxService;

	@Transactional
	public DepositResponse runDepositTransaction(DepositCommand depositCommand) {
		final Account account = accountReadService.loadAccountWithLock(depositCommand.accountId());

		accountValidator.validateAccountOwner(account.getMemberId(), depositCommand.memberId());
		accountValidator.validateAccountStatusNormal(account);
		accountValidator.validateAmountPositive(depositCommand.amount());

		accountWriteService.increaseBalance(account, depositCommand.amount());
		accountOutboxService.appendDepositEvent(account, depositCommand.amount(), depositCommand.description());

		return AccountMapper.toDepositResponse(account, depositCommand);
	}
}
