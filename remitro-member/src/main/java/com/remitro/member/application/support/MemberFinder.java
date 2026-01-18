package com.remitro.member.application.support;

import org.springframework.stereotype.Component;

import com.remitro.common.error.ErrorCode;
import com.remitro.common.exception.NotFoundException;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberFinder {

	private final MemberRepository memberRepository;

	public Member getMemberById(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
	}

	public Member getMemberByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
	}
}
