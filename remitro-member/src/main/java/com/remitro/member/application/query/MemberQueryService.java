package com.remitro.member.application.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.policy.MemberAccessPolicy;
import com.remitro.member.presentation.dto.response.MemberMeResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService {

	private final MemberFinder memberFinder;

	private final MemberAccessPolicy memberAccessPolicy;

	public MemberMeResponse getMyProfile(Long memberId) {
		final Member member = memberFinder.getById(memberId);

		memberAccessPolicy.validateAccessible(member);

		return MemberMeResponse.from(member);
	}
}
