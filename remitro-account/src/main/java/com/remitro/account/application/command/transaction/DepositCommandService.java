package com.remitro.account.application.command.transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.command.account.AccountAccessValidator;
import com.remitro.account.application.command.dto.request.DepositRequest;
import com.remitro.account.application.command.dto.response.DepositResponse;
import com.remitro.account.application.idempotency.IdempotencyProvider;
import com.remitro.account.application.mapper.AccountMapper;
import com.remitro.account.application.read.AccountFinder;
import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.account.policy.FormatPolicy;
import com.remitro.account.domain.ledger.enums.TransactionType;
import com.remitro.account.domain.ledger.model.AccountLedger;
import com.remitro.account.domain.ledger.repository.AccountLedgerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DepositCommandService {

	private final AccountFinder accountFinder;
	private final AccountLedgerRepository accountLedgerRepository;
	private final IdempotencyProvider idempotencyProvider;

	private final FormatPolicy formatPolicy;
	private final AccountAccessValidator accountAccessValidator;
	private final DepositValidator depositValidator;

	public DepositResponse deposit(Long memberId, Long accountId, String requestId, DepositRequest depositRequest) {
		idempotencyProvider.process(requestId, memberId);

		final Account account = accountFinder.getAccountByIdWithLock(accountId);

		accountAccessValidator.validateOwnership(account, memberId);
		depositValidator.validateDeposit(account, depositRequest.amount());

		account.deposit(depositRequest.amount());

		final AccountLedger accountLedger = recordLedger(account, requestId, depositRequest);
		accountLedgerRepository.save(accountLedger);

		return AccountMapper.toDepositResponse(
			accountLedger,
			formatPolicy.formatAccountNumber(account.getAccountNumber()),
			formatPolicy.formatAmount(depositRequest.amount()),
			formatPolicy.formatAmount(account.getBalance())
		);
	}

	private AccountLedger recordLedger(Account account, String requestId, DepositRequest depositRequest) {
		return AccountLedger.record(
			account.getId(),
			requestId,
			TransactionType.DEPOSIT,
			depositRequest.amount(),
			account.getBalance(),
			depositRequest.description()
		);
	}
}
