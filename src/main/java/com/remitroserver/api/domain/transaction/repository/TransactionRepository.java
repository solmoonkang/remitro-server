package com.remitroserver.api.domain.transaction.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.remitroserver.api.domain.account.entity.Account;
import com.remitroserver.api.domain.transaction.entity.Transaction;
import com.remitroserver.api.domain.transaction.model.TransactionStatus;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	boolean existsByIdempotencyKey(String idempotencyKey);

	Optional<Transaction> findByTransactionToken(UUID transactionToken);

	Optional<Transaction> findByTransactionTokenAndStatus(UUID transactionToken, TransactionStatus status);

	@Query("""
		SELECT t FROM Transaction t
		WHERE (t.fromAccount = :account OR t.toAccount = :account)
			AND t.createdAt >= :fromAt
		ORDER BY t.createdAt DESC
		""")
	List<Transaction> findTransactionsWithinPeriod(
		@Param("account") Account account,
		@Param("fromAt") LocalDateTime fromAt);

	@Query("""
		SELECT t FROM Transaction t
		WHERE (t.fromAccount = :account OR t.toAccount = :account)
			AND (:fromAt IS NULL OR t.createdAt >= :fromAt)
			AND (:toAt IS NULL OR t.createdAt <= :toAt)
			AND (:status IS NULL OR t.status = :status)
		ORDER BY t.createdAt DESC
		""")
	List<Transaction> findTransactionsByAccountAndCondition(
		@Param("account") Account account,
		@Param("fromAt") LocalDateTime fromAt,
		@Param("toAt") LocalDateTime toAt,
		@Param("status") TransactionStatus status
	);
}
