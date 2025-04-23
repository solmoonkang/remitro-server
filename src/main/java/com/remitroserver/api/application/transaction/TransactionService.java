package com.remitroserver.api.application.transaction;

import static com.remitroserver.api.domain.transaction.model.TransactionStatus.*;
import static com.remitroserver.global.error.model.ErrorMessage.*;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitroserver.api.application.account.AccountReadService;
import com.remitroserver.api.application.member.MemberReadService;
import com.remitroserver.api.application.transaction.mapper.TransactionMapper;
import com.remitroserver.api.domain.account.entity.Account;
import com.remitroserver.api.domain.account.model.Money;
import com.remitroserver.api.domain.auth.model.AuthMember;
import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.api.domain.transaction.entity.Transaction;
import com.remitroserver.api.domain.transaction.entity.TransactionStatusLog;
import com.remitroserver.api.domain.transaction.repository.TransactionRepository;
import com.remitroserver.api.domain.transaction.repository.TransactionStatusLogRepository;
import com.remitroserver.api.dto.transaction.request.TransactionSearchRequest;
import com.remitroserver.api.dto.transaction.request.TransferRequest;
import com.remitroserver.api.dto.transaction.response.TransactionDetailResponse;
import com.remitroserver.api.dto.transaction.response.TransactionSummaryResponse;
import com.remitroserver.global.error.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final TransactionStatusLogRepository transactionStatusLogRepository;
	private final MemberReadService memberReadService;
	private final AccountReadService accountReadService;
	private final TransactionReadService transactionReadService;

	@Transactional
	public void requestTransfer(AuthMember authMember, TransferRequest transferRequest) {
		transactionReadService.validateIdempotencyKeyExists(transferRequest.idempotencyKey());

		final Member member = memberReadService.getMemberByEmail(authMember.email());
		final Account fromAccount = accountReadService.getActiveAccountByTokenAndOwner(
			transferRequest.fromAccountToken(), member);
		final Account toAccount = accountReadService.getActiveAccountByAccountNumber(transferRequest.toAccountNumber());

		validateNotSelfTransfer(fromAccount, toAccount);

		final Money amount = new Money(transferRequest.amount());
		final Transaction transaction = Transaction.create(
			fromAccount, toAccount, amount, transferRequest.idempotencyKey());

		transactionRepository.save(transaction);
		transactionStatusLogRepository.save(TransactionStatusLog.create(transaction, COMPLETED));
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

		final List<TransactionStatusLog> transactionStatusLogs = transactionStatusLogRepository
			.findAllByTransactionOrderByCreatedAtAsc(transaction);

		return TransactionMapper.toDetailResponse(transaction, transactionStatusLogs);
	}

	@Transactional
	public void approveTransfer(UUID transactionToken, AuthMember authMember) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());
		final Transaction transaction = transactionReadService.getRequestedTransactionByTokenAndOwner(
			transactionToken, member);

		transaction.validateNotExpired(Duration.ofMinutes(5));
		transaction.complete();

		performTransfer(transaction.getFromAccount(), transaction.getToAccount(), transaction.getAmount());

		transactionStatusLogRepository.save(TransactionStatusLog.create(transaction, COMPLETED));
	}

	@Transactional
	public void cancelTransfer(UUID transactionToken, AuthMember authMember) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());
		final Transaction transaction = transactionReadService.getRequestedTransactionByTokenAndOwner(
			transactionToken, member);

		transaction.cancel();

		transactionStatusLogRepository.save(TransactionStatusLog.create(transaction, CANCELLED));
	}

	private void performTransfer(Account fromAccount, Account toAccount, Money amount) {
		fromAccount.withdraw(amount);
		toAccount.deposit(amount);
	}

	private void validateNotSelfTransfer(Account fromAccount, Account toAccount) {
		if (fromAccount.isSameAccount(toAccount)) {
			throw new BadRequestException(TRANSFER_TO_SAME_ACCOUNT_ERROR);
		}
	}
}
