package com.remitro.account.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.account.domain.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

	List<Account> findByMemberId(Long memberId);

	Optional<Account> findByAccountNumber(String accountNumber);
}
