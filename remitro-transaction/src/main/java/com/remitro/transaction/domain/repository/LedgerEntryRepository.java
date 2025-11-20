package com.remitro.transaction.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.transaction.domain.model.LedgerEntry;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {
}
