package com.remitro.member.application.common.support;

import org.springframework.stereotype.Component;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.NotFoundException;
import com.remitro.common.error.message.ErrorMessage;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberFinder {

	private final MemberRepository memberRepository;

	public Member getById(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.MEMBER_NOT_FOUND, ErrorMessage.MEMBER_NOT_FOUND)
			);
	}

	public Member getByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.MEMBER_NOT_FOUND, ErrorMessage.MEMBER_NOT_FOUND)
			);
	}
}
