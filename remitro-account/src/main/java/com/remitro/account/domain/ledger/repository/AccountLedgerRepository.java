package com.remitro.account.domain.ledger.repository;

import java.util.List;

import com.remitro.account.domain.ledger.model.AccountLedger;

public interface AccountLedgerRepository {

	AccountLedger save(AccountLedger accountLedger);

	List<AccountLedger> saveAll(List<AccountLedger> accountLedgers);
}
