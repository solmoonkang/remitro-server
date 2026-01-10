package com.remitro.account.infrastructure.persistence.account;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.account.domain.account.model.Account;

public interface SpringDataAccountRepository extends JpaRepository<Account, Long> {

	int countByMemberId(Long memberId);
}
