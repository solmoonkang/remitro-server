package com.remitro.member.application.command;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.command.dto.request.ProfileUpdateRequest;
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.policy.MemberUpdatePolicy;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileCommandService {

	private final MemberFinder memberFinder;
	private final MemberUpdatePolicy memberUpdatePolicy;

	@CacheEvict(value = "members", key = "'ID:' + #memberId")
	public void updateProfile(Long memberId, ProfileUpdateRequest profileUpdateRequest) {
		final Member member = memberFinder.getMemberById(memberId);

		memberUpdatePolicy.validateUniqueness(
			memberId,
			profileUpdateRequest.nickname(),
			profileUpdateRequest.phoneNumber()
		);

		member.updateProfile(
			profileUpdateRequest.nickname(),
			profileUpdateRequest.phoneNumber()
		);
	}
}
