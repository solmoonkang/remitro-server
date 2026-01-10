package com.remitro.account.domain.status.repository;

import com.remitro.account.domain.status.model.AccountStatusHistory;

public interface AccountStatusHistoryRepository {

	AccountStatusHistory save(AccountStatusHistory accountStatusHistory);
}
