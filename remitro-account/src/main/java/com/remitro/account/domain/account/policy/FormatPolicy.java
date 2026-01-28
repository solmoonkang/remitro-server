package com.remitro.account.domain.account.policy;

import org.springframework.stereotype.Component;

@Component
public class FormatPolicy {

	private static final String HYPHEN = "-";

	public String format(String accountNumber) {
		return new StringBuilder(14)
			.append(accountNumber, 0, 3)
			.append(HYPHEN)
			.append(accountNumber, 3, 5)
			.append(HYPHEN)
			.append(accountNumber, 5, 12)
			.toString();
	}
}
