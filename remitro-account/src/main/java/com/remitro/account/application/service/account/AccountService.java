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
import com.remitro.account.domain.enums.AccountStatusUpdateReason;
import com.remitro.account.domain.enums.ActorType;
import com.remitro.account.domain.enums.IdempotencyOperationType;
import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.Idempotency;
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
		final Idempotency idempotency = idempotencyService.acquireIdempotencyOrGet(
			idempotencyKey,
			memberId,
			IdempotencyOperationType.OPEN_ACCOUNT
		);

		if (idempotency.isCompleted()) {
			final Account existingAccount = accountReadService.findAccountById(idempotency.getResourceId());
			return AccountMapper.toOpenAccountCreationResponse(existingAccount);
		}

		final MemberProjection member = accountReadService.findMemberProjectionById(memberId);
		accountEligibilityValidator.validateMemberEligibleForAccount(member);

		final Account account = accountWriteService.saveAccount(member, openAccountRequest);

		accountOutboxService.appendAccountOpenedEventOutbox(account);

		idempotency.completeWithResource(account.getId());

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
	public void updateAccountStatusByMember(Long accountId, AccountStatus newStatus) {
		updateAccountStatusInternal(accountId, newStatus, ActorType.MEMBER, AccountStatusUpdateReason.USER_REQUEST);
	}

	@Transactional
	public void enforceAccountStatusUpdate(
		Long accountId,
		AccountStatus newStatus,
		AccountStatusUpdateReason accountStatusUpdateReason
	) {
		updateAccountStatusInternal(accountId, newStatus, ActorType.SYSTEM, accountStatusUpdateReason);
	}

	private void updateAccountStatusInternal(
		Long accountId,
		AccountStatus newStatus,
		ActorType actorType,
		AccountStatusUpdateReason accountStatusUpdateReason
	) {
		final Account account = accountReadService.findAccountById(accountId);
		final AccountStatus previousStatus = account.getAccountStatus();

		accountStatusTransactionValidator.validateStatusTransition(previousStatus, newStatus);

		account.applyAccountStatus(newStatus);

		accountOutboxService.appendAccountStatusUpdatedEventOutbox(
			account,
			previousStatus,
			actorType,
			accountStatusUpdateReason
		);
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
