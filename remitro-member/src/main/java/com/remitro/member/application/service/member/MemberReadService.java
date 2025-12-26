package com.remitro.member.application.service.member;

import org.springframework.stereotype.Service;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.NotFoundException;
import com.remitro.common.error.message.ErrorMessage;
import com.remitro.member.domain.model.Member;
import com.remitro.member.domain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberReadService {

	private final MemberRepository memberRepository;

	public Member findMemberById(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.MEMBER_NOT_FOUND,
				ErrorMessage.MEMBER_NOT_FOUND)
			);
	}

	public Member findMemberByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.MEMBER_NOT_FOUND,
				ErrorMessage.MEMBER_NOT_FOUND)
			);
	}
}
