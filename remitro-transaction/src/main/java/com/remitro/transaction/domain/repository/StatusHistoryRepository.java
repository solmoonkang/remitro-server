package com.remitro.transaction.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.transaction.domain.model.AccountStatusHistory;

public interface StatusHistoryRepository extends JpaRepository<AccountStatusHistory, Long> {

	boolean existsByEventId(String eventId);
}
