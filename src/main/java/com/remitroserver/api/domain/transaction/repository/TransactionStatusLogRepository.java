package com.remitroserver.api.domain.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitroserver.api.domain.transaction.entity.Transaction;
import com.remitroserver.api.domain.transaction.entity.TransactionStatusLog;

public interface TransactionStatusLogRepository extends JpaRepository<TransactionStatusLog, Long> {

	List<TransactionStatusLog> findAllByTransactionOrderByCreatedAtAsc(Transaction transaction);
}
