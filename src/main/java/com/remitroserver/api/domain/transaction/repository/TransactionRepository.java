package com.remitroserver.api.domain.transaction.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.api.domain.transaction.entity.Transaction;
import com.remitroserver.api.domain.transaction.model.TransactionStatus;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	boolean existsByIdempotencyKey(String idempotencyKey);

	Optional<Transaction> findByTransactionTokenAndFromAccountMemberAndStatus(
		UUID transactionToken, Member member, TransactionStatus status);
}
