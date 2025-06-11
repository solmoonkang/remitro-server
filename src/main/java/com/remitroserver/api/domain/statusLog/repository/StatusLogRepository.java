package com.remitroserver.api.domain.statusLog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitroserver.api.domain.transaction.entity.Transaction;
import com.remitroserver.api.domain.statusLog.entity.StatusLog;

public interface StatusLogRepository extends JpaRepository<StatusLog, Long> {

	List<StatusLog> findAllByTransactionOrderByCreatedAtAsc(Transaction transaction);
}
