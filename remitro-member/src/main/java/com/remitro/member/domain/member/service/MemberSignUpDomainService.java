package com.remitro.member.domain.member.service;

import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.policy.MemberSignUpPolicy;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberSignUpDomainService {

	private final MemberSignUpPolicy memberSignUpPolicy;

	public Member signUp(
		String email,
		String hashedPassword,
		String nickname,
		String phoneNumber,
		boolean emailExists,
		boolean nicknameExists,
		boolean phoneExists
	) {
		memberSignUpPolicy.validateDuplicated(emailExists, nicknameExists, phoneExists);
		return Member.signUp(email, hashedPassword, nickname, phoneNumber);
	}
}
