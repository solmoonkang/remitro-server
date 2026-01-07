package com.remitro.member.application.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.policy.MemberAccessPolicy;
import com.remitro.member.domain.member.policy.MemberProfilePolicy;
import com.remitro.member.domain.member.repository.MemberQueryRepository;
import com.remitro.member.presentation.dto.request.UpdateProfileRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateProfileCommandService {

	private final MemberFinder memberFinder;
	private final MemberQueryRepository memberQueryRepository;

	private final MemberAccessPolicy memberAccessPolicy;
	private final MemberProfilePolicy memberProfilePolicy;

	public void updateProfile(Long memberId, UpdateProfileRequest updateProfileRequest) {
		final Member member = memberFinder.getById(memberId);

		memberAccessPolicy.validateAccessible(member);

		final boolean nicknameExists = memberQueryRepository.existsByNickname(updateProfileRequest.nickname());
		final boolean phoneExists = memberQueryRepository.existsByPhoneNumber(updateProfileRequest.phoneNumber());

		memberProfilePolicy.validateDuplicated(nicknameExists, phoneExists);

		member.updateProfile(updateProfileRequest.nickname(), updateProfileRequest.phoneNumber());
	}
}
