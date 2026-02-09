package com.remitro.member.application.read;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.repository.MemberRepository;
import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberFinder {

	private final MemberRepository memberRepository;

	public Member getMemberById(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
	}

	public Member getActiveById(Long memberId) {
		return memberRepository.findActiveById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
	}

	@Cacheable(value = "memberSession", key = "'EMAIL:' + #email")
	public Member getActiveByEmail(String email) {
		return memberRepository.findActiveByEmail(email)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
	}

	public Member getMemberByNicknameAndPhoneNumber(String nickname, String phoneNumber) {
		return memberRepository.findByNicknameAndPhoneNumber(nickname, phoneNumber)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
	}
}
