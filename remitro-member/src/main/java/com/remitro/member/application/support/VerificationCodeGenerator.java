package com.remitro.member.application.support;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class VerificationCodeGenerator {

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final int CODE_LENGTH = 6;

	private final SecureRandom secureRandom = new SecureRandom();

	public String generate() {
		return secureRandom.ints(CODE_LENGTH, 0, CHARACTERS.length())
			.mapToObj(CHARACTERS::charAt)
			.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
			.toString();
	}
}
