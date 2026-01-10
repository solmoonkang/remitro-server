package com.remitro.account.infrastructure.persistence.account;

import org.springframework.stereotype.Repository;

import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.account.repository.AccountCommandRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaAccountCommandRepository implements AccountCommandRepository {

	private final SpringDataAccountRepository springDataAccountRepository;

	@Override
	public Account save(Account account) {
		return springDataAccountRepository.save(account);
	}
}
