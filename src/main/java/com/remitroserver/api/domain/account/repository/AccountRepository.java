package com.remitroserver.api.domain.account.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.remitroserver.api.domain.account.entity.Account;
import com.remitroserver.api.domain.account.model.AccountType;
import com.remitroserver.api.domain.member.entity.Member;

public interface AccountRepository extends JpaRepository<Account, Long> {

	boolean existsByAccountNumber(String accountNumber);

	int countByMemberAndAccountType(Member member, AccountType accountType);

	@Query("SELECT a FROM Account a WHERE a.member = :member ORDER BY a.createdAt DESC")
	List<Account> findAccountsByMemberOrderByCreatedAt(@Param("member") Member member);

	Optional<Account> findByAccountTokenAndMember(UUID accessToken, Member member);

	Optional<Account> findByAccountNumber(String accountNumber);
}
