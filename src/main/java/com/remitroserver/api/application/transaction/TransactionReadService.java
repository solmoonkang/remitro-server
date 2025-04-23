package com.remitroserver.api.application.transaction;

import static com.remitroserver.global.error.model.ErrorMessage.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.remitroserver.api.domain.account.entity.Account;
import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.api.domain.transaction.entity.Transaction;
import com.remitroserver.api.domain.transaction.model.TransactionStatus;
import com.remitroserver.api.domain.transaction.repository.TransactionRepository;
import com.remitroserver.api.dto.transaction.request.TransactionSearchRequest;
import com.remitroserver.global.error.exception.ConflictException;
import com.remitroserver.global.error.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionReadService {

	private final TransactionRepository transactionRepository;

	public Transaction getRequestedTransactionByTokenAndOwner(UUID transactionToken, Member member) {
		return transactionRepository
			.findByTransactionTokenAndFromAccountMemberAndStatus(transactionToken, member, TransactionStatus.REQUESTED)
			.orElseThrow(() -> new NotFoundException(TRANSACTION_NOT_FOUND_ERROR));
	}

	public List<Transaction> getRecentTransactions(Account account) {
		final LocalDateTime fromAt = LocalDate.now().withDayOfMonth(1).atStartOfDay();
		return transactionRepository.findTransactionsWithinPeriod(account, fromAt);
	}

	public List<Transaction> getAllTransactionsByCondition(
		Account account,
		TransactionSearchRequest transactionSearchRequest) {

		final LocalDateTime fromAt = Optional.ofNullable(transactionSearchRequest.fromDate())
			.map(LocalDate::atStartOfDay)
			.orElse(null);

		final LocalDateTime toAt = Optional.ofNullable(transactionSearchRequest.toDate())
			.map(date -> date.atTime(LocalTime.MAX))
			.orElse(null);

		return transactionRepository.findTransactionsByAccountAndCondition(
			account, fromAt, toAt, transactionSearchRequest.status());
	}

	public Transaction getTransactionByTokenAndOwner(UUID transactionToken, Member member) {
		return transactionRepository.findByTransactionTokenAndMember(transactionToken, member)
			.orElseThrow(() -> new NotFoundException(TRANSACTION_NOT_FOUND_ERROR));
	}

	public void validateIdempotencyKeyExists(String idempotencyKey) {
		if (transactionRepository.existsByIdempotencyKey(idempotencyKey)) {
			throw new ConflictException(DUPLICATED_IDEMPOTENCY_KEY_ERROR);
		}
	}
}
