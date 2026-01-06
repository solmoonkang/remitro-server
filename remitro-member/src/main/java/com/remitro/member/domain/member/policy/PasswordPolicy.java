package com.remitro.member.domain.member.policy;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.message.ErrorMessage;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.presentation.dto.request.ChangePasswordRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordPolicy {

	private final PasswordEncoder passwordEncoder;

	public void validateChangeable(Member member, ChangePasswordRequest changePasswordRequest) {
		if (!passwordEncoder.matches(changePasswordRequest.currentPassword(), member.getHashedPassword())) {
			throw new BadRequestException(
				ErrorCode.PASSWORD_INVALID, ErrorMessage.PASSWORD_INVALID
			);
		}

		if (!changePasswordRequest.newPassword().equals(changePasswordRequest.confirmPassword())) {
			throw new BadRequestException(
				ErrorCode.INVALID_REQUEST, ErrorMessage.INVALID_REQUEST
			);
		}

		if (passwordEncoder.matches(changePasswordRequest.newPassword(), member.getHashedPassword())) {
			throw new BadRequestException(
				ErrorCode.INVALID_REQUEST, ErrorMessage.INVALID_REQUEST
			);
		}
	}
}
