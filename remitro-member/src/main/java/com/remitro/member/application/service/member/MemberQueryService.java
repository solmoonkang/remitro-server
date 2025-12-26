package com.remitro.member.application.service.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.dto.response.MemberInfoResponse;
import com.remitro.member.application.mapper.MemberMapper;
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.domain.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService {

	private final MemberFinder memberFinder;

	public MemberInfoResponse getMemberInfo(Long memberId) {
		final Member member = memberFinder.getById(memberId);
		return MemberMapper.toMemberInfoResponse(member);
	}
}
