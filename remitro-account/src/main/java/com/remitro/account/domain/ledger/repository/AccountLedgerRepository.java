package com.remitro.account.domain.ledger.repository;

import com.remitro.account.domain.ledger.model.AccountLedger;

public interface AccountLedgerRepository {

	AccountLedger save(AccountLedger accountLedger);
}
