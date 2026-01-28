package com.remitro.account.domain.account.repository;

import com.remitro.account.domain.account.model.Account;

public interface AccountRepository {

	Account save(Account account);

	int countByMemberId(Long memberId);
}
