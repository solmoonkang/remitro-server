package com.remitro.account.domain.account.repository;

import com.remitro.account.domain.account.model.Account;

public interface AccountCommandRepository {

	Account save(Account account);
}
