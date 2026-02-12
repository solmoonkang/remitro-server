package com.remitro.account.domain.account.repository;

import java.util.Optional;

import com.remitro.account.domain.account.enums.AccountType;
import com.remitro.account.domain.account.model.Account;

public interface AccountRepository {

	Account save(Account account);

	int countByMemberIdAndAccountType(Long memberId, AccountType accountType);

	Optional<Account> findByIdWithLock(Long id);
}
