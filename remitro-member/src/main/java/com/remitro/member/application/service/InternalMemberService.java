package com.remitro.member.application.service;

import org.springframework.stereotype.Service;

import com.remitro.common.contract.member.MemberAuthMapper;
import com.remitro.common.contract.member.MemberCredentialsResponse;
import com.remitro.member.domain.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InternalMemberService {

	private final MemberReadService memberReadService;

	public MemberCredentialsResponse findAuthInfo(String email) {
		final Member member = memberReadService.findMemberByEmail(email);
		return MemberAuthMapper.toMemberCredentialsResponse(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			member.getHashedPassword()
		);
	}
}
