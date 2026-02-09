package com.remitro.account.domain.account.policy;

import java.text.DecimalFormat;

import org.springframework.stereotype.Component;

@Component
public class FormatPolicy {

	private static final String HYPHEN = "-";
	private static final String DEFAULT_AMOUNT_TEXT = "0";
	private static final DecimalFormat AMOUNT_FORMAT = new DecimalFormat("#,###");

	public String formatAccountNumber(String accountNumber) {
		return accountNumber.substring(0, 3) + HYPHEN +
			   accountNumber.substring(3, 5) + HYPHEN +
			   accountNumber.substring(5, 12);
	}

	public String formatAmount(Long amount) {
		if (amount == null)
			return DEFAULT_AMOUNT_TEXT;

		return AMOUNT_FORMAT.format(amount);
	}
}
