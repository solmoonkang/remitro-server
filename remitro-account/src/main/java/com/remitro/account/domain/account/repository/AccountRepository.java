package com.remitro.account.domain.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.account.domain.product.enums.ProductType;
import com.remitro.account.domain.account.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

	boolean existsByAccountNumber(String accountNumber);

	long countByMemberId(Long memberId);

	Long countByMemberIdAndProductType(Long memberId, ProductType productType);

	List<Account> findAllByMemberId(Long memberId);
}
