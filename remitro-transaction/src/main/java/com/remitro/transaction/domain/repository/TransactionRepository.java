package com.remitro.transaction.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.remitro.transaction.domain.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	@Query("SELECT t FROM Transaction t "
		+ "WHERE t.senderAccount.id = :accountId OR t.receiverAccount.id = :accountId "
		+ "ORDER BY t.transactionAt DESC")
	List<Transaction> findAllByAccountIdOrderByDateDesc(@Param("accountId") Long accountId);
}
