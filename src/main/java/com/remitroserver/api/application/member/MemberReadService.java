package com.remitroserver.api.application.member;

import static com.remitroserver.global.error.model.ErrorMessage.*;

import org.springframework.stereotype.Service;

import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.api.domain.member.repository.MemberRepository;
import com.remitroserver.global.error.exception.ConflictException;
import com.remitroserver.global.error.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberReadService {

	private final MemberRepository memberRepository;

	public Member getMemberByEmail(String email) {
		return memberRepository.findMemberByEmail(email)
			.orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND_ERROR));
	}

	public void validateEmailDuplicated(String email) {
		if (memberRepository.existsMemberByEmail(email)) {
			throw new ConflictException(DUPLICATED_EMAIL_ERROR);
		}
	}

	public void validateNicknameDuplicated(String nickname) {
		if (memberRepository.existsMemberByNickname(nickname)) {
			throw new ConflictException(DUPLICATED_NICKNAME_ERROR);
		}
	}

	public void validateRegistrationNumberDuplicated(String registrationNumber) {
		if (memberRepository.existsMemberByRegistrationNumber(registrationNumber)) {
			throw new ConflictException(DUPLICATED_REGISTRATION_NUMBER_ERROR);
		}
	}
}
