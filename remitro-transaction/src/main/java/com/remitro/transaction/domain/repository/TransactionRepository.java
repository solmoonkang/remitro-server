package com.remitro.transaction.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.transaction.domain.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	boolean existsByEventId(String eventId);
}
