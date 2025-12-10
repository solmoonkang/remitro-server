package com.remitro.account.application.service.account;

import static com.remitro.common.util.constant.RedisConstant.*;

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
import com.remitro.account.application.service.balance.AccountBalanceService;
import com.remitro.account.application.service.balance.DistributedLockManager;
import com.remitro.account.application.service.idempotency.IdempotencyService;
import com.remitro.account.application.service.outbox.AccountOutboxService;
import com.remitro.account.application.validator.AccountEligibilityValidator;
import com.remitro.account.application.validator.AccountStatusTransactionValidator;
import com.remitro.account.domain.enums.AccountStatus;
import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.MemberProjection;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

	private final AccountEligibilityValidator accountEligibilityValidator;
	private final AccountReadService accountReadService;
	private final AccountWriteService accountWriteService;
	private final IdempotencyService idempotencyService;
	private final DistributedLockManager distributedLockManager;
	private final AccountBalanceService accountBalanceService;
	private final AccountOutboxService accountOutboxService;
	private final AccountStatusTransactionValidator accountStatusTransactionValidator;

	@Transactional
	public OpenAccountCreationResponse openAccount(
		Long memberId,
		String idempotencyKey,
		OpenAccountRequest openAccountRequest
	) {
		idempotencyService.validateOpenAccountIdempotency(memberId, idempotencyKey, OPEN_ACCOUNT_PREFIX);

		final MemberProjection member = accountReadService.findMemberProjectionById(memberId);
		accountEligibilityValidator.validateMemberIsActive(member);

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
		return accountBalanceService.findAccountBalance(memberId, accountId);
	}

	@Transactional
	public void changeAccountStatus(Long accountId, AccountStatus targetStatus) {
		final Account account = accountReadService.findAccountById(accountId);
		final AccountStatus currentStatus = account.getAccountStatus();
		accountStatusTransactionValidator.validateStatusTransition(currentStatus, targetStatus);
		account.applyAccountStatus(targetStatus);
		accountOutboxService.appendAccountStatusUpdatedEvent(account, currentStatus);
	}

	public DepositResponse deposit(DepositCommand depositCommand) {
		idempotencyService.validateBalanceUpdatedIdempotency(
			depositCommand.memberId(),
			depositCommand.accountId(),
			depositCommand.idempotencyKey(),
			DEPOSIT_PREFIX
		);

		return distributedLockManager.executeWithAccountLock(
			depositCommand.accountId(),
			() -> accountBalanceService.runDepositTransaction(depositCommand)
		);
	}
}
