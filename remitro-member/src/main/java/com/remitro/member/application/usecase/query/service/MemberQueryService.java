package com.remitro.member.application.usecase.query.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.usecase.query.dto.response.MemberInfoResponse;
import com.remitro.member.application.common.mapper.MemberMapper;
import com.remitro.member.application.common.support.MemberFinder;
import com.remitro.member.domain.member.model.Member;

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
