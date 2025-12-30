package com.remitro.account.application.common.support.generator;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;

import com.remitro.account.domain.product.enums.ProductType;
import com.remitro.account.domain.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountNumberGenerator {

	private static final int RANDOM_DIGITS = 10;

	private final AccountRepository accountRepository;

	public String generate(ProductType productType) {
		String accountNumber;

		do {
			accountNumber = generateCandidateNumber(productType);
		} while (accountRepository.existsByAccountNumber(accountNumber));

		return accountNumber;
	}

	private String generateCandidateNumber(ProductType productType) {
		return productType.getCode() + RandomStringUtils.randomNumeric(RANDOM_DIGITS);
	}
}
