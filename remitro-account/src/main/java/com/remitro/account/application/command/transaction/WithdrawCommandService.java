package com.remitro.account.application.command.transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.command.account.validator.AccountAccessValidator;
import com.remitro.account.application.command.account.validator.PinNumberValidator;
import com.remitro.account.application.command.dto.request.WithdrawRequest;
import com.remitro.account.application.command.dto.response.WithdrawResponse;
import com.remitro.account.application.command.transaction.validator.WithdrawValidator;
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
public class WithdrawCommandService {

	private final AccountFinder accountFinder;
	private final AccountLedgerRepository accountLedgerRepository;
	private final IdempotencyProvider idempotencyProvider;

	private final FormatPolicy formatPolicy;
	private final AccountAccessValidator accountAccessValidator;
	private final PinNumberValidator pinNumberValidator;
	private final WithdrawValidator withdrawValidator;

	public WithdrawResponse withdraw(Long memberId, Long accountId, String requestId, WithdrawRequest withdrawRequest) {
		idempotencyProvider.process(requestId, memberId);

		final Account account = accountFinder.getAccountByIdWithLock(accountId);

		accountAccessValidator.validateOwnership(account, memberId);
		withdrawValidator.validateWithdraw(account, withdrawRequest.amount());
		pinNumberValidator.validatePinMatch(withdrawRequest.pinNumber(), account.getPinNumberHash());

		account.withdraw(withdrawRequest.amount());

		final AccountLedger accountLedger = recordLedger(account, requestId, withdrawRequest);
		accountLedgerRepository.save(accountLedger);

		return AccountMapper.toWithdrawResponse(
			accountLedger,
			formatPolicy.formatAccountNumber(account.getAccountNumber()),
			formatPolicy.formatAmount(withdrawRequest.amount()),
			formatPolicy.formatAmount(account.getBalance())
		);
	}

	private AccountLedger recordLedger(Account account, String requestId, WithdrawRequest withdrawRequest) {
		return AccountLedger.record(
			account.getId(),
			requestId,
			TransactionType.WITHDRAW,
			withdrawRequest.amount(),
			account.getBalance(),
			withdrawRequest.description()
		);
	}
}
