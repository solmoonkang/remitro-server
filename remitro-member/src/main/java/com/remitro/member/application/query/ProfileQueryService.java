package com.remitro.member.application.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.mapper.MemberMapper;
import com.remitro.member.application.query.dto.response.MemberProfileResponse;
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.policy.DataMaskingPolicy;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileQueryService {

	private final MemberFinder memberFinder;
	private final DataMaskingPolicy dataMaskingPolicy;

	public MemberProfileResponse getMyProfile(Long memberId) {
		final Member member = memberFinder.getMemberById(memberId);

		final String maskEmail = dataMaskingPolicy.maskEmail(member.getEmail());
		final String maskPhoneNumber = dataMaskingPolicy.maskPhoneNumber(member.getPhoneNumber());

		return MemberMapper.toMemberProfileResponse(maskEmail, member.getNickname(), maskPhoneNumber);
	}
}
