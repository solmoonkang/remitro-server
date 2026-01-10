package com.remitro.member.application.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.auth.MemberAuthInfo;
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.domain.member.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberAuthInfoQueryService {

	private final MemberFinder memberFinder;

	public MemberAuthInfo findForLogin(String email) {
		final Member member = memberFinder.getByEmail(email);

		return new MemberAuthInfo(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			member.getHashedPassword(),
			member.getRole()
		);
	}
}
