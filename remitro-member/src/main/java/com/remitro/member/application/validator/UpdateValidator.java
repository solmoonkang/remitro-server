package com.remitro.member.application.validator;

import org.springframework.stereotype.Component;

import com.remitro.common.error.ErrorCode;
import com.remitro.common.exception.BadRequestException;
import com.remitro.member.domain.member.policy.UpdatePolicy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateValidator {

	private final UpdatePolicy updatePolicy;

	public void validateProfileUpdateUniqueness(Long memberId, String nickname, String phoneNumber) {
		if (updatePolicy.isNicknameAlreadyUsed(memberId, nickname)) {
			throw new BadRequestException(ErrorCode.DUPLICATE_NICKNAME);
		}

		if (updatePolicy.isPhoneNumberAlreadyUsed(memberId, phoneNumber)) {
			throw new BadRequestException(ErrorCode.DUPLICATE_PHONE_NUMBER);
		}
	}
}
