package com.remitro.account.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.account.repository.AccountRepository;

public interface JpaAccountRepository extends JpaRepository<Account, Long>, AccountRepository {
}
