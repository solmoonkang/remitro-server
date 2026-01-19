package com.remitro.member.domain.member.policy;

import com.remitro.common.error.ErrorCode;
import com.remitro.common.exception.BadRequestException;
import com.remitro.member.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberUpdatePolicy {

	private final MemberRepository memberRepository;

	public void validateUniqueness(Long memberId, String nickname, String phoneNumber) {
		if (memberRepository.existsByNicknameAndIdNot(nickname, memberId)) {
			throw new BadRequestException(ErrorCode.DUPLICATE_NICKNAME);
		}

		if (memberRepository.existsByPhoneNumberAndIdNot(phoneNumber, memberId)) {
			throw new BadRequestException(ErrorCode.DUPLICATE_PHONE_NUMBER);
		}
	}
}
