package com.remitro.member.application.usecase.password.service;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.message.ErrorMessage;
import com.remitro.member.application.usecase.password.dto.request.ChangePasswordRequest;
import com.remitro.member.application.common.support.MemberFinder;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.infrastructure.messaging.MemberEventPublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberPasswordCommandService {

	private final MemberFinder memberFinder;
	private final PasswordEncoder passwordEncoder;
	private final Clock clock;
	private final MemberEventPublisher memberEventPublisher;

	@Transactional
	public void changePassword(Long memberId, ChangePasswordRequest changePasswordRequest) {
		final Member member = memberFinder.getById(memberId);

		validateCurrentPasswordMatches(
			member.getHashedPassword(), changePasswordRequest.currentPassword()
		);
		validateNewPasswordIsDifferentFromCurrent(
			changePasswordRequest.currentPassword(), changePasswordRequest.newPassword()
		);
		validatePasswordConfirmationMatches(
			changePasswordRequest.newPassword(), changePasswordRequest.confirmPassword()
		);

		final String newHashedPassword = passwordEncoder.encode(changePasswordRequest.newPassword());
		member.changePassword(newHashedPassword);

		memberEventPublisher.publishMemberPasswordChanged(member, LocalDateTime.now(clock));
	}

	private void validateCurrentPasswordMatches(String savedHashedPassword, String currentPassword) {
		if (!passwordEncoder.matches(currentPassword, savedHashedPassword)) {
			throw new BadRequestException(ErrorCode.PASSWORD_INVALID, ErrorMessage.PASSWORD_INVALID);
		}
	}

	private void validateNewPasswordIsDifferentFromCurrent(String currentPassword, String newPassword) {
		if (currentPassword.equals(newPassword)) {
			throw new BadRequestException(ErrorCode.PASSWORD_INVALID, ErrorMessage.PASSWORD_INVALID);
		}
	}

	private void validatePasswordConfirmationMatches(String newPassword, String confirmPassword) {
		if (!newPassword.equals(confirmPassword)) {
			throw new BadRequestException(ErrorCode.PASSWORD_INVALID, ErrorMessage.PASSWORD_INVALID);
		}
	}
}
