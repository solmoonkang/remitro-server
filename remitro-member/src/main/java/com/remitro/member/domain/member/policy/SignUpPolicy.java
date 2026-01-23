package com.remitro.member.domain.member.policy;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SignUpPolicy {

	private static final int REJOIN_RESTRICTION_DAYS = 30;

	private final MemberRepository memberRepository;

	public boolean isEmailDuplicated(String email) {
		return memberRepository.existsByEmail(email);
	}

	public boolean isNicknameDuplicated(String nickname) {
		return memberRepository.existsByNickname(nickname);
	}

	public boolean isPhoneNumberHashDuplicated(String phoneNumberHash) {
		return memberRepository.existsByPhoneNumberHash(phoneNumberHash);
	}

	public boolean isRejoinRestricted(Member withdrawnMember, LocalDateTime now) {
		if (withdrawnMember == null) {
			return false;
		}
		return withdrawnMember.getWithdrawnAt()
			.plusDays(REJOIN_RESTRICTION_DAYS)
			.isAfter(now);
	}
}
