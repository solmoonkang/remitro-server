package com.remitro.member.application.common.validator;

import org.springframework.stereotype.Component;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.exception.ConflictException;
import com.remitro.common.error.message.ErrorMessage;
import com.remitro.member.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberValidator {

	private final MemberRepository memberRepository;

	public void validateUniqueEmail(String email) {
		if (memberRepository.existsByEmail(email)) {
			throw new ConflictException(ErrorCode.EMAIL_ALREADY_EXISTS, ErrorMessage.EMAIL_ALREADY_EXISTS);
		}
	}

	public void validateUniqueNickname(String nickname) {
		if (memberRepository.existsByNickname(nickname)) {
			throw new ConflictException(ErrorCode.NICKNAME_ALREADY_EXISTS, ErrorMessage.NICKNAME_ALREADY_EXISTS);
		}
	}

	public void validateUniquePhoneNumber(String phoneNumber) {
		if (memberRepository.existsByPhoneNumber(phoneNumber)) {
			throw new ConflictException(
				ErrorCode.PHONE_NUMBER_ALREADY_EXISTS, ErrorMessage.PHONE_NUMBER_ALREADY_EXISTS
			);
		}
	}

	public void validatePasswordMatches(String password, String checkPassword) {
		if (!password.equals(checkPassword)) {
			throw new BadRequestException(ErrorCode.PASSWORD_INVALID, ErrorMessage.PASSWORD_INVALID);
		}
	}
}
