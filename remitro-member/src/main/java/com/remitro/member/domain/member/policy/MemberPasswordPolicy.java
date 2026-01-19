package com.remitro.member.domain.member.policy;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.remitro.common.error.ErrorCode;
import com.remitro.common.exception.BadRequestException;
import com.remitro.common.exception.UnauthorizedException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberPasswordPolicy {

	private final PasswordEncoder passwordEncoder;

	public void validatePasswordMatch(String rawPassword, String encodedPassword) {
		if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
			throw new UnauthorizedException(ErrorCode.INVALID_PASSWORD);
		}
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
