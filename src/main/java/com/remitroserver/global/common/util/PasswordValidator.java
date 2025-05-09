package com.remitroserver.global.common.util;

import static com.remitroserver.global.error.model.ErrorMessage.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.remitroserver.global.error.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordValidator {

	private final PasswordEncoder passwordEncoder;

	public void validatePasswordEquals(String rawPassword, String checkPassword) {
		if (!rawPassword.equals(checkPassword)) {
			throw new BadRequestException(PASSWORD_MISMATCH_ERROR);
		}
	}

	public void validatePasswordMatches(String rawPassword, String encodedPassword) {
		if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
			throw new BadRequestException(PASSWORD_MISMATCH_ERROR);
		}
	}
}
