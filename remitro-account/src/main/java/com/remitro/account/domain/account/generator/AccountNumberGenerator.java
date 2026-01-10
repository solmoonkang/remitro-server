package com.remitro.account.domain.account.generator;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.enums.ProductType;

@Component
public class AccountNumberGenerator {

	public String generate(ProductType productType) {
		return switch (productType.getCategory()) {
			case DEPOSIT -> generateDepositAccountNumber(productType);
			case LOAN -> generateLoanAccount(productType);
			case VIRTUAL -> generateVirtualAccount(productType);
		};
	}

	private String generateDepositAccountNumber(ProductType productType) {
		return productType.getCode() + "-" + generateChannelCode() + "-" + generateSerialNumber(6);
	}

	private String generateLoanAccount(ProductType productType) {
		return productType.getCode() + "-" + generateSerialNumber(7);
	}

	private String generateVirtualAccount(ProductType productType) {
		return productType.getCode() + "-" + generateSerialNumber(10);
	}

	private String generateChannelCode() {
		return String.format("%03d", ThreadLocalRandom.current().nextInt(0, 1000));
	}

	private String generateSerialNumber(int length) {
		final long bound = (long)Math.pow(10, length);
		return String.format("%0" + length + "d", ThreadLocalRandom.current().nextLong(bound));
	}
}
