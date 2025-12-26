package com.remitro.member.application.service.member;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.remitro.member.application.dto.request.UpdateMemberProfileRequest;
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.application.validator.MemberValidator;
import com.remitro.member.domain.model.Member;
import com.remitro.member.infrastructure.messaging.MemberEventPublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberProfileCommandService {

	private final MemberValidator memberValidator;
	private final MemberFinder memberFinder;
	private final Clock clock;
	private final MemberEventPublisher memberEventPublisher;

	@Transactional
	public void updateProfile(Long memberId, UpdateMemberProfileRequest updateMemberProfileRequest) {
		final Member member = memberFinder.getById(memberId);

		boolean profileUpdated = false;
		profileUpdated |= updateNicknameIfNeeded(member, updateMemberProfileRequest.nickname());
		profileUpdated |= updatePhoneNumberIfNeeded(member, updateMemberProfileRequest.phoneNumber());

		if (profileUpdated) {
			memberEventPublisher.publishMemberProfileUpdated(member, LocalDateTime.now(clock));
		}
	}

	private boolean updateNicknameIfNeeded(Member member, String newNickname) {
		if (!StringUtils.hasText(newNickname) && !newNickname.equals(member.getNickname())) {
			return false;
		}

		memberValidator.validateUniqueNickname(newNickname);
		member.updateNickname(newNickname);
		return true;
	}

	private boolean updatePhoneNumberIfNeeded(Member member, String newPhoneNumber) {
		if (!StringUtils.hasText(newPhoneNumber) && !newPhoneNumber.equals(member.getPhoneNumber())) {
			return false;
		}

		memberValidator.validateUniquePhoneNumber(newPhoneNumber);
		member.updatePhoneNumber(newPhoneNumber);
		return true;
	}
}
