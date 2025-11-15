package com.remitro.member.application.service;

import org.springframework.stereotype.Service;

import com.remitro.common.infra.error.exception.NotFoundException;
import com.remitro.common.infra.error.model.ErrorMessage;
import com.remitro.member.domain.model.Member;
import com.remitro.member.domain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberReadService {

	private final MemberRepository memberRepository;

	public Member findMemberById(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND));
	}

	public Member findMemberByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND));
	}
}
