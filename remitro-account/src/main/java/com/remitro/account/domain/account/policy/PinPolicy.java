package com.remitro.account.domain.account.policy;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PinPolicy {

	private final PasswordEncoder passwordEncoder;

	public String encode(String rawPin) {
		return passwordEncoder.encode(rawPin);
	}
}
