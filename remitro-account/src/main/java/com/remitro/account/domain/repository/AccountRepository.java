package com.remitro.account.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.enums.AccountStatus;

public interface AccountRepository extends JpaRepository<Account, Long> {

	boolean existsByAccountNumber(String accountNumber);

	Optional<Account> findByIdAndMemberId(Long accountId, Long memberId);

	List<Account> findAccountsByMemberId(Long memberId);

	@Query("SELECT a FROM Account a WHERE a.id = :accountId")
	Optional<Account> findByIdWithLock(Long accountId);

	List<Account> findByLastTransactionAtBeforeAndAccountStatus(LocalDateTime lastTime, AccountStatus accountStatus);
}
