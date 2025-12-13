package com.remitro.account.application.service.balance;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.dto.request.deposit.DepositCommand;
import com.remitro.account.application.dto.response.AccountBalanceResponse;
import com.remitro.account.application.dto.response.DepositResponse;
import com.remitro.account.application.mapper.AccountMapper;
import com.remitro.account.application.service.outbox.AccountOutboxService;
import com.remitro.account.application.service.account.AccountReadService;
import com.remitro.account.application.service.account.AccountWriteService;
import com.remitro.account.application.validator.AccountOperationPolicy;
import com.remitro.account.application.validator.AccountTransactionValidator;
import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.repository.AccountBalanceRedisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountBalanceService {

	private final AccountOperationPolicy accountOperationPolicy;
	private final AccountTransactionValidator accountTransactionValidator;
	private final AccountReadService accountReadService;
	private final AccountWriteService accountWriteService;
	private final AccountOutboxService accountOutboxService;
	private final AccountBalanceRedisRepository accountBalanceRedisRepository;

	public AccountBalanceResponse findAccountBalance(Long memberId, Long accountId) {
		final Account account = accountReadService.findAccountByIdAndMemberId(memberId, accountId);
		final Long balance = accountBalanceRedisRepository.findCachedBalance(accountId)
			.orElseGet(() -> cacheBalanceFromAccount(account));
		return AccountMapper.toAccountBalanceResponse(balance);
	}

	private Long cacheBalanceFromAccount(Account account) {
		final Long balance = account.getBalance();
		accountBalanceRedisRepository.saveCachedBalance(account.getId(), balance);
		return balance;
	}

	@Transactional
	public DepositResponse runDepositTransaction(DepositCommand depositCommand) {
		final Account account = accountReadService.loadAccountWithLock(depositCommand.accountId());

		accountTransactionValidator.validateAccountOwnership(account.getMemberId(), depositCommand.memberId());
		accountOperationPolicy.validateDepositAllowed(account);
		accountTransactionValidator.validatePositiveAmount(depositCommand.amount());

		accountWriteService.increaseBalance(account, depositCommand.amount());
		accountBalanceRedisRepository.saveCachedBalance(account.getId(), account.getBalance());
		accountOutboxService.appendDepositEvent(account, depositCommand.amount(), depositCommand.description());

		return AccountMapper.toDepositResponse(account, depositCommand);
	}
}
