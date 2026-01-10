package com.remitro.account.domain.account.repository;

import java.util.Optional;

import com.remitro.account.domain.account.model.Account;

public interface AccountQueryRepository {

	Optional<Account> findById(Long accountId);

	int countByMemberId(Long memberId);
}
