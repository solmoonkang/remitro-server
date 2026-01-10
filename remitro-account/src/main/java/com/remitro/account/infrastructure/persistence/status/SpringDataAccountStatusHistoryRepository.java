package com.remitro.account.infrastructure.persistence.status;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.account.domain.status.model.AccountStatusHistory;

public interface SpringDataAccountStatusHistoryRepository extends JpaRepository<AccountStatusHistory, Long> {

	List<AccountStatusHistory> findAllByAccountId(Long accountId);
}
