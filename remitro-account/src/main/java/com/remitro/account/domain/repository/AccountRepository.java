package com.remitro.account.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.remitro.account.domain.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

	@Query("SELECT a FROM Account a WHERE a.id = :accountId")
	Optional<Account> findByAccountId(@Param("accountId") Long accountId);

	List<Account> findByMemberId(Long memberId);

	Optional<Account> findByAccountNumber(String accountNumber);

	@Query("SELECT a.id FROM Account a WHERE a.accountNumber = :accountNumber")
	Optional<Long> findAccountIdByAccountNumber(@Param("accountNumber") String accountNumber);
}
