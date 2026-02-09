package com.remitro.account.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.account.repository.AccountRepository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;

public interface JpaAccountRepository extends JpaRepository<Account, Long>, AccountRepository {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@QueryHints(@QueryHint(name = "javax.persistence.lock.timeout", value = "3000"))
	@Query("SELECT a FROM Account a WHERE a.id = :id")
	Optional<Account> findByIdWithLock(@Param("id") Long id);
}
