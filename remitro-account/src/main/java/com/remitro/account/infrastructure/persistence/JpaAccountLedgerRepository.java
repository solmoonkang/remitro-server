package com.remitro.account.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.account.domain.ledger.model.AccountLedger;
import com.remitro.account.domain.ledger.repository.AccountLedgerRepository;

public interface JpaAccountLedgerRepository extends JpaRepository<AccountLedger, Long>, AccountLedgerRepository {
}
