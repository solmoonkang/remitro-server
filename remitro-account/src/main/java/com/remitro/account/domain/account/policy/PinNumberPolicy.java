package com.remitro.account.domain.account.policy;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PinNumberPolicy {

	private final PasswordEncoder passwordEncoder;

	public boolean isWrongPinNumber(String rawPinNumber, String encodedPinNumber) {
		return !passwordEncoder.matches(rawPinNumber, encodedPinNumber);
	}

	public boolean isSamePinNumber(String rawPinNumber, String confirmPinNumber) {
		return rawPinNumber.equals(confirmPinNumber);
	}
}
