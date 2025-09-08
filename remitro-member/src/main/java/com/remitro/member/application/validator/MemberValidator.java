package com.remitro.member.application.validator;

import org.springframework.stereotype.Component;

import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.exception.ConflictException;
import com.remitro.common.error.model.ErrorMessage;
import com.remitro.member.domain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberValidator {

	private final MemberRepository memberRepository;

	public void validateEmailNotDuplicated(String email) {
		if (memberRepository.existsByEmail(email)) {
			throw new ConflictException(ErrorMessage.EMAIL_DUPLICATED);
		}
	}

	public void validateNicknameNotDuplicated(String nickname) {
		if (memberRepository.existsByNickname(nickname)) {
			throw new ConflictException(ErrorMessage.NICKNAME_DUPLICATED);
		}
	}

	public void validatePasswordMatche(String password, String checkPassword) {
		if (!password.equals(checkPassword)) {
			throw new BadRequestException(ErrorMessage.INVALID_PASSWORD);
		}
	}
}
