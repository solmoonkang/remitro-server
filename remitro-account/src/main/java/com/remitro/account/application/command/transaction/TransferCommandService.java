package com.remitro.account.application.command.transaction;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.command.account.validator.AccountAccessValidator;
import com.remitro.account.application.command.account.validator.PinNumberValidator;
import com.remitro.account.application.command.dto.request.TransferRequest;
import com.remitro.account.application.command.dto.response.TransferResponse;
import com.remitro.account.application.command.transaction.validator.TransferValidator;
import com.remitro.account.application.idempotency.IdempotencyProvider;
import com.remitro.account.application.mapper.AccountMapper;
import com.remitro.account.application.read.AccountFinder;
import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.account.policy.FormatPolicy;
import com.remitro.account.domain.ledger.enums.TransactionType;
import com.remitro.account.domain.ledger.model.AccountLedger;
import com.remitro.account.domain.ledger.repository.AccountLedgerRepository;
import com.remitro.account.infrastructure.aop.DistributedLock;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TransferCommandService {

	private final AccountFinder accountFinder;
	private final AccountLedgerRepository accountLedgerRepository;
	private final IdempotencyProvider idempotencyProvider;

	private final FormatPolicy formatPolicy;
	private final AccountAccessValidator accountAccessValidator;
	private final TransferValidator transferValidator;
	private final PinNumberValidator pinNumberValidator;

	@DistributedLock(
		fromAccountId = "#fromAccountId",
		toAccountId = "#toAccountId"
	)
	public TransferResponse transfer(
		Long memberId,
		Long fromAccountId,
		Long toAccountId,
		String requestId,
		TransferRequest transferRequest
	) {
		idempotencyProvider.process(requestId, memberId);

		final Account fromAccount = accountFinder.getAccountByIdWithLock(fromAccountId);
		final Account toAccount = accountFinder.getAccountByIdWithLock(toAccountId);

		accountAccessValidator.validateOwnership(fromAccount, memberId);
		pinNumberValidator.validatePinMatch(transferRequest.pinNumber(), fromAccount.getPinNumberHash());
		transferValidator.validateTransfer(fromAccount, toAccount, transferRequest.amount());

		fromAccount.withdraw(transferRequest.amount());
		toAccount.deposit(transferRequest.amount());

		final List<AccountLedger> accountLedgers = recordLedgers(fromAccount, toAccount, requestId, transferRequest);
		final List<AccountLedger> savedLedgers = accountLedgerRepository.saveAll(accountLedgers);

		return AccountMapper.toTransferResponse(
			savedLedgers.get(0),
			savedLedgers.get(1),
			formatPolicy.formatAccountNumber(fromAccount.getAccountNumber()),
			formatPolicy.formatAccountNumber(toAccount.getAccountNumber()),
			formatPolicy.formatAmount(transferRequest.amount()),
			formatPolicy.formatAmount(fromAccount.getBalance())
		);
	}

	private List<AccountLedger> recordLedgers(
		Account fromAccount,
		Account toAccount,
		String requestId,
		TransferRequest transferRequest
	) {
		return List.of(
			AccountLedger.record(fromAccount.getId(), requestId, TransactionType.TRANSFER_OUT,
				transferRequest.amount(), fromAccount.getBalance(), transferRequest.toDescription()
			),
			AccountLedger.record(toAccount.getId(), requestId, TransactionType.TRANSFER_IN,
				transferRequest.amount(), toAccount.getBalance(), transferRequest.fromDescription()
			)
		);
	}
}
