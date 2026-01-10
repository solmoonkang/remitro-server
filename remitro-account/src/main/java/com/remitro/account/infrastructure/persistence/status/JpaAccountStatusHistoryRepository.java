package com.remitro.account.infrastructure.persistence.status;

import org.springframework.stereotype.Repository;

import com.remitro.account.domain.status.model.AccountStatusHistory;
import com.remitro.account.domain.status.repository.AccountStatusHistoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaAccountStatusHistoryRepository implements AccountStatusHistoryRepository {

	private final SpringDataAccountStatusHistoryRepository springDataAccountStatusHistoryRepository;

	@Override
	public AccountStatusHistory save(AccountStatusHistory accountStatusHistory) {
		return springDataAccountStatusHistoryRepository.save(accountStatusHistory);
	}
}
