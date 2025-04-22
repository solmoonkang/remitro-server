package com.remitroserver.api.domain.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitroserver.api.domain.transaction.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	boolean existsByIdempotencyKey(String idempotencyKey);
}
