package com.remitroserver.api.application.transaction;

import static com.remitroserver.global.common.util.RedisConstant.*;
import static com.remitroserver.global.error.model.ErrorMessage.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitroserver.api.application.account.AccountReadService;
import com.remitroserver.api.application.member.MemberReadService;
import com.remitroserver.api.application.transaction.mapper.TransactionMapper;
import com.remitroserver.api.domain.account.entity.Account;
import com.remitroserver.api.domain.account.model.Money;
import com.remitroserver.api.domain.auth.model.AuthMember;
import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.api.domain.statusLog.entity.StatusLog;
import com.remitroserver.api.domain.statusLog.repository.StatusLogRepository;
import com.remitroserver.api.domain.transaction.entity.Transaction;
import com.remitroserver.api.dto.account.request.AccountAmountRequest;
import com.remitroserver.api.dto.transaction.request.TransactionSearchRequest;
import com.remitroserver.api.dto.transaction.request.TransferRequest;
import com.remitroserver.api.dto.transaction.response.TransactionDetailResponse;
import com.remitroserver.api.dto.transaction.response.TransactionSummaryResponse;
import com.remitroserver.global.error.exception.BadRequestException;
import com.remitroserver.global.lock.aop.DistributedLock;
import com.remitroserver.global.lock.core.DistributedLockExecutor;
import com.remitroserver.global.lock.core.RetryExecutor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

	private final StatusLogRepository statusLogRepository;
	private final MemberReadService memberReadService;
	private final AccountReadService accountReadService;
	private final TransactionReadService transactionReadService;
	private final TransactionWriteService transactionWriteService;
	private final DistributedLockExecutor distributedLockExecutor;
	private final RetryExecutor retryExecutor;

	@DistributedLock(key = "#accountToken")
	public void requestDeposit(UUID accountToken, AuthMember authMember, AccountAmountRequest accountAmountRequest) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());

		retryExecutor.execute(() ->
			transactionWriteService.processDeposit(accountToken, member, accountAmountRequest.amount()));
	}

	@DistributedLock(key = "#accountToken")
	public void requestWithdraw(UUID accountToken, AuthMember authMember, AccountAmountRequest accountAmountRequest) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());

		retryExecutor.execute(() ->
			transactionWriteService.processWithdraw(
				accountToken, member, accountAmountRequest.amount(), accountAmountRequest.accountPassword()));
	}

	public void requestTransfer(AuthMember authMember, String idempotencyKey, TransferRequest transferRequest) {
		transactionReadService.validateIdempotencyKeyNotExists(idempotencyKey);

		final Member member = memberReadService.getMemberByEmail(authMember.email());
		final Account fromAccount = getSenderAccount(transferRequest.fromAccountToken(), member);
		final Account toAccount = getRecipientAccount(transferRequest.toAccountNumber());

		validateNotSelfTransfer(fromAccount, toAccount);
		executeTransferWithLock(fromAccount, toAccount, transferRequest.amount(), idempotencyKey);
	}

	public List<TransactionSummaryResponse> findMyAllTransactionsByCondition(
		UUID accountToken,
		AuthMember authMember,
		TransactionSearchRequest transactionSearchRequest) {

		final Member member = memberReadService.getMemberByEmail(authMember.email());
		final Account account = accountReadService.getAccountByTokenAndOwner(accountToken, member);
		final List<Transaction> transactions = transactionReadService.getAllTransactionsByCondition(
			account, transactionSearchRequest);

		return transactions.stream()
			.map(TransactionMapper::toSummaryResponse)
			.toList();
	}

	public TransactionDetailResponse findTransactionDetail(UUID transactionToken, AuthMember authMember) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());
		final Transaction transaction = transactionReadService.getTransactionByTokenAndOwner(transactionToken, member);

		final List<StatusLog> statusLogs = statusLogRepository
			.findAllByTransactionOrderByCreatedAtAsc(transaction);

		return TransactionMapper.toDetailResponse(transaction, statusLogs);
	}

	@Transactional
	public void approveTransfer(UUID transactionToken, AuthMember authMember) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());
		transactionWriteService.completeTransactionWithLog(transactionToken, member);

		final String idempotencyKey = transactionReadService.getIdempotencyKeyByTransactionToken(transactionToken);
		transactionWriteService.createTransactionIdempotencyKey(idempotencyKey);
	}

	@Transactional
	public void cancelTransfer(UUID transactionToken, AuthMember authMember) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());
		transactionWriteService.cancelTransactionWithLog(transactionToken, member);
	}

	private void validateNotSelfTransfer(Account fromAccount, Account toAccount) {
		if (fromAccount.isSameAccount(toAccount)) {
			throw new BadRequestException(TRANSFER_TO_SAME_ACCOUNT_ERROR);
		}
	}

	private Account getSenderAccount(UUID fromToken, Member member) {
		return accountReadService.getActiveAccountByTokenAndOwner(fromToken, member);
	}

	private Account getRecipientAccount(String toAccountNumber) {
		return accountReadService.getActiveAccountByAccountNumber(toAccountNumber);
	}

	private List<String> getSortedAccountLockKeys(Long fromAccountId, Long toAccountId) {
		return Stream.of(fromAccountId, toAccountId)
			.sorted()
			.map(accountId -> ACCOUNT_LOCK_KEY_PREFIX + accountId)
			.toList();
	}

	private void executeTransferWithLock(Account from, Account to, Long rawAmount, String idempotencyKey) {
		final List<String> lockKeys = getSortedAccountLockKeys(from.getId(), to.getId());

		distributedLockExecutor.executeWithMultiLocks(lockKeys, () -> {
			final Money amount = Money.fromPositive(rawAmount);
			final Transaction transaction = transactionWriteService.createRequestedTransactionWithLog(from, to, amount);
			transactionWriteService.storeTransactionTokenMapping(transaction.getTransactionToken(), idempotencyKey);
		});
	}
}
