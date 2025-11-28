package com.remitro.member.application.service.internal;

import org.springframework.stereotype.Service;

import com.remitro.common.contract.MemberAuthInfo;
import com.remitro.member.application.mapper.MemberAuthMapper;
import com.remitro.member.application.service.member.MemberReadService;
import com.remitro.member.domain.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InternalMemberService {

	private final MemberReadService memberReadService;

	public MemberAuthInfo findAuthInfo(String email) {
		final Member member = memberReadService.findMemberByEmail(email);
		return MemberAuthMapper.toMemberCredentialsResponse(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			member.getHashedPassword()
		);
	}
}
