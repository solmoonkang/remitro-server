package com.remitro.transaction.domain.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.remitro.transaction.domain.model.LedgerEntry;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {

	@Query("SELECT le.balanceAfter FROM LedgerEntry le "
		+ "JOIN le.transaction t WHERE t.accountId = :accountId ORDER BY le.occurredAt DESC, le.id DESC"
	)
	List<Long> findLatestLedgerEntry(@Param("accountId") Long accountId, Pageable pageable);

	@Query("SELECT le FROM LedgerEntry le WHERE le.transaction.accountId = :accountId ORDER BY le.occurredAt ASC")
	List<LedgerEntry> findAllByAccountIdOrderByOccurredAtAsc(@Param("accountId") Long accountId);

	@Query("SELECT DISTINCT le.transaction.accountId FROM LedgerEntry le")
	Set<Long> findDistinctAccountIds();
}
