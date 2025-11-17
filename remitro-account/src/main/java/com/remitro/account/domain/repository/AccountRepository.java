package com.remitro.account.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.account.domain.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

	boolean existsByAccountNumber(String accountNumber);

	Optional<Account> findByIdAndMemberId(Long accountId, Long memberId);

	List<Account> findAccountsByMemberId(Long memberId);
}
