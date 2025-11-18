package com.remitro.account.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.dto.request.OpenAccountRequest;
import com.remitro.account.application.dto.request.deposit.DepositCommand;
import com.remitro.account.application.dto.response.AccountBalanceResponse;
import com.remitro.account.application.dto.response.AccountDetailResponse;
import com.remitro.account.application.dto.response.AccountsSummaryResponse;
import com.remitro.account.application.dto.response.DepositResponse;
import com.remitro.account.application.dto.response.OpenAccountCreationResponse;
import com.remitro.account.application.mapper.AccountMapper;
import com.remitro.account.application.service.distributedlock.DistributedLockManager;
import com.remitro.account.application.validator.AccountValidator;
import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.MemberProjection;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

	public static final String OPEN_ACCOUNT_PREFIX = "OPEN_ACCOUNT:";
	public static final String DEPOSIT_PREFIX = "DEPOSIT:";

	private final AccountValidator accountValidator;
	private final AccountReadService accountReadService;
	private final AccountWriteService accountWriteService;
	private final IdempotencyService idempotencyService;
	private final AccountOutboxService accountOutboxService;
	private final DistributedLockManager distributedLockManager;

	@Transactional
	public OpenAccountCreationResponse openAccount(
		Long memberId,
		String idempotencyKey,
		OpenAccountRequest openAccountRequest) {

		idempotencyService.validateIdempotencyFirstRequest(memberId, idempotencyKey, OPEN_ACCOUNT_PREFIX);

		final MemberProjection member = accountReadService.findMemberProjectionById(memberId);
		accountValidator.validateMemberIsActive(member);

		final Account account = accountWriteService.saveAccount(member, openAccountRequest);
		accountWriteService.appendAccountOpenedEventOutbox(account);

		return AccountMapper.toOpenAccountCreationResponse(account);
	}

	public AccountDetailResponse findAccountDetail(Long memberId, Long accountId) {
		final Account account = accountReadService.findAccountByIdAndMemberId(memberId, accountId);
		return AccountMapper.toAccountDetailResponse(account);
	}

	public AccountsSummaryResponse findMemberAccounts(Long memberId) {
		final MemberProjection member = accountReadService.findMemberProjectionById(memberId);
		final List<Account> accounts = accountReadService.findAllAccountByMemberId(memberId);
		return AccountMapper.toAccountsSummaryResponse(member, accounts);
	}

	public AccountBalanceResponse findAccountBalance(Long memberId, Long accountId) {
		final Account account = accountReadService.findAccountByIdAndMemberId(memberId, accountId);
		return AccountMapper.toAccountBalanceResponse(account);
	}

	// TODO: 현재까지 입금 서비스가 제대로 동작하는지를 한 번 더 검증해야 하며, 추가로 Kafka를 통해 Transaction 모듈까지의 흐름을 연결시켜야 한다.

	public DepositResponse deposit(DepositCommand depositCommand) {
		idempotencyService.validateIdempotencyFirstRequest(
			depositCommand.memberId(),
			depositCommand.idempotencyKey(),
			DEPOSIT_PREFIX
		);

		return distributedLockManager.executeWithAccountLock(
			depositCommand.accountId(),
			() -> runDepositTransaction(depositCommand)
		);
	}

	@Transactional
	protected DepositResponse runDepositTransaction(DepositCommand depositCommand) {
		final Account account = accountReadService.loadAccountWithLock(depositCommand.accountId());

		accountValidator.validateAccountOwner(account.getMemberId(), depositCommand.memberId());
		accountValidator.validateAccountStatusNormal(account.getAccountStatus());
		accountValidator.validateAmountPositive(depositCommand.amount());

		accountWriteService.increaseBalance(account, depositCommand.amount());
		accountOutboxService.appendDepositEvent(account, depositCommand.amount());

		return AccountMapper.toDepositResponse(account, depositCommand);
	}
}
