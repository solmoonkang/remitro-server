package com.remitro.member.domain.member.policy;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.remitro.common.error.ErrorCode;
import com.remitro.common.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberPasswordPolicy {

	private final PasswordEncoder passwordEncoder;

	public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

	public void validatePasswordChange(
		String encodedPassword,
		String currentPassword,
		String newPassword,
		String confirmPassword
	) {
		if (!passwordEncoder.matches(currentPassword, encodedPassword)) {
			throw new BadRequestException(ErrorCode.WRONG_PASSWORD);
		}

		if (currentPassword.equals(newPassword)) {
			throw new BadRequestException(ErrorCode.PASSWORD_REUSE_DENIED);
		}

		if (!newPassword.equals(confirmPassword)) {
			throw new BadRequestException(ErrorCode.PASSWORD_CONFIRM_MISMATCH);
		}
	}
}
