package com.remitroserver.api.application.transaction;

import static com.remitroserver.global.error.model.ErrorMessage.*;

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

	private final TransactionStatusLogRepository transactionStatusLogRepository;
	private final MemberReadService memberReadService;
	private final AccountReadService accountReadService;
	private final TransactionReadService transactionReadService;
	private final TransactionWriteService transactionWriteService;

	@Transactional
	public void requestTransfer(AuthMember authMember, String idempotencyKey, TransferRequest transferRequest) {
		transactionReadService.validateIdempotencyKeyExists(idempotencyKey);

		final Member member = memberReadService.getMemberByEmail(authMember.email());
		final Account fromAccount = accountReadService.getActiveAccountByTokenAndOwner(
			transferRequest.fromAccountToken(), member);
		final Account toAccount = accountReadService.getActiveAccountByAccountNumber(transferRequest.toAccountNumber());

		validateNotSelfTransfer(fromAccount, toAccount);

		final Money amount = Money.fromPositive(transferRequest.amount());

		transactionWriteService.createTransactionWithLog(fromAccount, toAccount, amount, idempotencyKey);
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
		transactionWriteService.completeTransactionWithLog(transactionToken, member);
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
}
