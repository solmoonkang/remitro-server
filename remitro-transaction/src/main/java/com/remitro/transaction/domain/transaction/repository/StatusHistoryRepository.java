package com.remitro.transaction.domain.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.transaction.domain.transaction.model.AccountStatusHistory;

public interface StatusHistoryRepository extends JpaRepository<AccountStatusHistory, Long> {

	boolean existsByEventId(String eventId);
}
