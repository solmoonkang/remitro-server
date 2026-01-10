package com.remitro.member.application.support;

import org.springframework.stereotype.Component;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.NotFoundException;
import com.remitro.common.error.message.ErrorMessage;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.repository.MemberQueryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberFinder {

	private final MemberQueryRepository memberQueryRepository;

	public Member getById(Long memberId) {
		return memberQueryRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.MEMBER_NOT_FOUND, ErrorMessage.MEMBER_ID_NOT_FOUND, memberId
			));
	}

	public Member getByEmail(String email) {
		return memberQueryRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.MEMBER_NOT_FOUND, ErrorMessage.MEMBER_EMAIL_NOT_FOUND, email
			));
	}
}
