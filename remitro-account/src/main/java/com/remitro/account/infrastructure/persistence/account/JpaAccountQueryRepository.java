package com.remitro.account.infrastructure.persistence.account;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.account.repository.AccountQueryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaAccountQueryRepository implements AccountQueryRepository {

	private final SpringDataAccountRepository springDataAccountRepository;

	@Override
	public Optional<Account> findById(Long accountId) {
		return springDataAccountRepository.findById(accountId);
	}

	@Override
	public int countByMemberId(Long memberId) {
		return springDataAccountRepository.countByMemberId(memberId);
	}

}
