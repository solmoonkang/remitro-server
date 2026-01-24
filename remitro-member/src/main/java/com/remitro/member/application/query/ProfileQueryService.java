package com.remitro.member.application.query;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.mapper.MemberMapper;
import com.remitro.member.application.query.dto.response.MemberProfileResponse;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.policy.MaskingPolicy;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileQueryService {

	private final MemberFinder memberFinder;
	private final MaskingPolicy maskingPolicy;

	@Cacheable(value = "memberProfile", key = "'ID:' + #memberId")
	public MemberProfileResponse getMyProfile(Long memberId) {
		final Member member = memberFinder.getMemberById(memberId);

		final String maskEmail = maskingPolicy.maskEmailForProfile(member.getEmail());
		final String maskPhoneNumber = maskingPolicy.maskPhoneNumberForProfile(member.getPhoneNumber());

		return MemberMapper.toMemberProfileResponse(maskEmail, member.getNickname(), maskPhoneNumber);
	}
}
