package com.remitro.account.application.usecase.open.service;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.usecase.open.command.AccountOpenCommand;
import com.remitro.account.application.usecase.open.command.DepositAccountOpenCommand;
import com.remitro.account.application.usecase.open.command.LoanAccountOpenCommand;
import com.remitro.account.application.usecase.open.command.VirtualAccountOpenCommand;
import com.remitro.account.application.usecase.open.dto.request.OpenDepositRequest;
import com.remitro.account.application.usecase.open.dto.request.OpenLoanRequest;
import com.remitro.account.application.usecase.open.dto.request.OpenVirtualRequest;
import com.remitro.account.application.usecase.open.dto.response.OpenAccountCreationResponse;
import com.remitro.account.application.usecase.open.mapper.AccountOpenMapper;
import com.remitro.account.application.usecase.open.processor.AccountOpenProcessor;
import com.remitro.account.application.usecase.open.processor.context.OpenedAccountContext;
import com.remitro.account.application.common.support.AccountFinder;
import com.remitro.account.application.common.support.AccountOpenProcessorRegistry;
import com.remitro.account.application.common.support.MemberProjectionFinder;
import com.remitro.account.application.common.support.service.IdempotencyCommandService;
import com.remitro.account.application.common.validator.AccountValidator;
import com.remitro.account.domain.idempotency.enums.IdempotencyOperationType;
import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.member.model.MemberProjection;
import com.remitro.account.domain.account.repository.AccountRepository;
import com.remitro.account.domain.loan.repository.LoanRepository;
import com.remitro.account.domain.idempotency.model.Idempotency;
import com.remitro.account.infrastructure.messaging.AccountEventPublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountOpenCommandService {

	private final AccountValidator accountValidator;
	private final IdempotencyCommandService idempotencyCommandService;
	private final AccountFinder accountFinder;
	private final MemberProjectionFinder memberProjectionFinder;
	private final AccountOpenProcessorRegistry accountOpenProcessorRegistry;

	private final PasswordEncoder passwordEncoder;
	private final AccountRepository accountRepository;
	private final LoanRepository loanRepository;

	private final Clock clock;
	private final AccountEventPublisher accountEventPublisher;

	@Transactional
	public OpenAccountCreationResponse openDepositAccount(
		Long memberId,
		String idempotencyKey,
		OpenDepositRequest openDepositRequest
	) {
		final AccountOpenCommand accountOpenCommand = new DepositAccountOpenCommand(
			memberId,
			openDepositRequest.accountName(),
			passwordEncoder.encode(openDepositRequest.pinNumber()),
			openDepositRequest.productType()
		);

		return open(memberId, idempotencyKey, accountOpenCommand);
	}

	@Transactional
	public OpenAccountCreationResponse openLoanAccount(
		Long memberId,
		String idempotencyKey,
		OpenLoanRequest openLoanRequest
	) {
		final AccountOpenCommand accountOpenCommand = new LoanAccountOpenCommand(
			memberId,
			openLoanRequest.productType(),
			openLoanRequest.loanAmount(),
			openLoanRequest.interestRate()
		);

		return open(memberId, idempotencyKey, accountOpenCommand);
	}

	@Transactional
	public OpenAccountCreationResponse openVirtualAccount(
		Long memberId,
		String idempotencyKey,
		OpenVirtualRequest openVirtualRequest
	) {
		final AccountOpenCommand accountOpenCommand = new VirtualAccountOpenCommand(
			memberId,
			openVirtualRequest.productType()
		);

		return open(memberId, idempotencyKey, accountOpenCommand);
	}

	private OpenAccountCreationResponse open(
		Long memberId,
		String idempotencyKey,
		AccountOpenCommand accountOpenCommand
	) {
		final Idempotency idempotency = idempotencyCommandService.acquireOrGet(
			memberId,
			idempotencyKey,
			IdempotencyOperationType.OPEN_ACCOUNT
		);

		if (idempotency.isSucceeded()) {
			return AccountOpenMapper.toOpenAccountCreationResponse(
				accountFinder.getById(idempotency.getResourceId())
			);
		}

		final MemberProjection member = memberProjectionFinder.getById(memberId);
		accountValidator.validateAccountOpenAllowed(member, accountOpenCommand);

		final AccountOpenProcessor accountOpenProcessor = accountOpenProcessorRegistry.get(
			accountOpenCommand.productType()
		);

		final OpenedAccountContext openedAccountContext = accountOpenProcessor.open(
			member,
			accountOpenCommand,
			LocalDateTime.now(clock)
		);

		final Account account = accountRepository.save(openedAccountContext.account());
		publishEvents(openedAccountContext, LocalDateTime.now(clock));
		idempotency.markSucceeded(account.getId());

		return AccountOpenMapper.toOpenAccountCreationResponse(account);
	}

	private void publishEvents(OpenedAccountContext openedAccountContext, LocalDateTime now) {
		openedAccountContext.loan().ifPresent(loan -> {
			loanRepository.save(loan);
			accountEventPublisher.publishLoanCreated(loan, LocalDateTime.now(clock));
		});

		accountEventPublisher.publishAccountOpened(openedAccountContext.account(), now);
	}
}
