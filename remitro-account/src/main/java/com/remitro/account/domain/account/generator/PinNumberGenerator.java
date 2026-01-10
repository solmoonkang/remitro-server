package com.remitro.account.domain.account.generator;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class PinNumberGenerator {

	private final SecureRandom secureRandom = new SecureRandom();

	public String generate4Digits() {
		return String.format("%04d", secureRandom.nextInt(10_000));
	}
}
