package com.remitro.transaction.domain.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.transaction.domain.aml.model.SuspiciousTransaction;

public interface SuspiciousTransactionRepository extends JpaRepository<SuspiciousTransaction, Long> {
}
