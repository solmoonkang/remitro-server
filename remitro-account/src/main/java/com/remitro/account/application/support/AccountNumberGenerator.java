package com.remitro.account.application.support;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.enums.AccountType;
import com.remitro.account.domain.account.repository.AccountNumberSequenceRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountNumberGenerator {

	private static final String CHANNEL_CODE = "99";

	private final AccountNumberSequenceRepository accountNumberSequenceRepository;

	public String generate(AccountType accountType) {
		final Long sequence = accountNumberSequenceRepository.nextSequence(accountType.getPrefix());
		return String.format("%s%s%07d", accountType.getPrefix(), CHANNEL_CODE, sequence);
	}
}
