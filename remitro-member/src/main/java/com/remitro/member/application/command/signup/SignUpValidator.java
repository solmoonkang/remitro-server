package com.remitro.member.application.command.signup;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.BadRequestException;
import com.remitro.support.exception.ConflictException;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.policy.SignUpPolicy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SignUpValidator {

	private final SignUpPolicy signUpPolicy;

	public void validateSignUpUniqueness(String email, String nickname, String phoneNumberHash) {
		if (signUpPolicy.isEmailDuplicated(email)) {
			throw new ConflictException(ErrorCode.DUPLICATE_EMAIL);
		}

		if (signUpPolicy.isNicknameDuplicated(nickname)) {
			throw new ConflictException(ErrorCode.DUPLICATE_NICKNAME);
		}

		if (signUpPolicy.isPhoneNumberHashDuplicated(phoneNumberHash)) {
			throw new ConflictException(ErrorCode.DUPLICATE_PHONE_NUMBER);
		}
	}

	public void validateRejoinRestriction(Member member, LocalDateTime now) {
		if (signUpPolicy.isRejoinRestricted(member, now)) {
			throw new BadRequestException(ErrorCode.REJOIN_RESTRICTED_PERIOD);
		}
	}
}
