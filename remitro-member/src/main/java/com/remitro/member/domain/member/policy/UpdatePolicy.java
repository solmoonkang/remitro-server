package com.remitro.member.domain.member.policy;

import org.springframework.stereotype.Component;

import com.remitro.member.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdatePolicy {

	private final MemberRepository memberRepository;

	public boolean isNicknameAlreadyUsed(Long memberId, String nickname) {
		return memberRepository.existsByNicknameAndIdNot(nickname, memberId);
	}

	public boolean isPhoneNumberAlreadyUsed(Long memberId, String phoneNumber) {
		return memberRepository.existsByPhoneNumberAndIdNot(phoneNumber, memberId);
	}
}
