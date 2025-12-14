package com.remitro.transaction.domain.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.transaction.domain.transaction.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	boolean existsByEventId(String eventId);
}
