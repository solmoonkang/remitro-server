package com.remitroserver.api.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitroserver.api.domain.account.entity.Account;
import com.remitroserver.api.domain.account.model.AccountType;
import com.remitroserver.api.domain.member.entity.Member;

public interface AccountRepository extends JpaRepository<Account, Long> {

	boolean existsByAccountNumber(String accountNumber);

	int countByMemberAndAccountType(Member member, AccountType accountType);
}
