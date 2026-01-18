package com.remitro.member.domain.member.policy;

import com.remitro.common.error.ErrorCode;
import com.remitro.common.exception.ConflictException;
import com.remitro.member.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberSignUpPolicy {

	private final MemberRepository memberRepository;

	public void validateUniqueness(String email, String nickname, String phoneNumber) {
		if (memberRepository.existsByEmail(email)) {
			throw new ConflictException(ErrorCode.DUPLICATE_EMAIL, email);
		}

		if (memberRepository.existsByNickname(nickname)) {
			throw new ConflictException(ErrorCode.DUPLICATE_NICKNAME, nickname);
		}

		if (memberRepository.existsByPhoneNumber(phoneNumber)) {
			throw new ConflictException(ErrorCode.DUPLICATE_PHONE_NUMBER, phoneNumber);
		}
	}
}
