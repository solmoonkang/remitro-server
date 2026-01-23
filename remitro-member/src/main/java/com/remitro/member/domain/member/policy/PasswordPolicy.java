package com.remitro.member.domain.member.policy;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordPolicy {

	private final PasswordEncoder passwordEncoder;

	public boolean isWrongPassword(String rawPassword, String encodedPassword) {
		return !passwordEncoder.matches(rawPassword, encodedPassword);
	}

	public boolean isSamePassword(String currentPassword, String newPassword) {
		return currentPassword.equals(newPassword);
	}

	public boolean isConfirmationMatched(String newPassword, String confirmPassword) {
		return newPassword.equals(confirmPassword);
	}
}
