package com.remitro.member.application.command.account;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.event.common.EventType;
import com.remitro.member.application.command.account.validator.UpdateValidator;
import com.remitro.member.application.dto.request.ProfileUpdateRequest;
import com.remitro.member.application.mapper.EventMapper;
import com.remitro.member.application.outbox.OutboxEventRecorder;
import com.remitro.member.application.read.MemberFinder;
import com.remitro.member.domain.member.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileCommandService {

	private final MemberFinder memberFinder;
	private final UpdateValidator updateValidator;

	private final OutboxEventRecorder outboxEventRecorder;
	private final Clock clock;

	@CacheEvict(value = "memberProfile", key = "'ID:' + #memberId")
	public void updateProfile(Long memberId, ProfileUpdateRequest profileUpdateRequest) {
		final LocalDateTime now = LocalDateTime.now(clock);
		final Member member = memberFinder.getActiveById(memberId);

		updateValidator.validateProfileUpdateUniqueness(
			memberId,
			profileUpdateRequest.nickname(),
			profileUpdateRequest.phoneNumber()
		);

		member.updateProfile(
			profileUpdateRequest.nickname(),
			profileUpdateRequest.phoneNumber()
		);

		outboxEventRecorder.record(
			EventType.MEMBER_PROFILE_UPDATED,
			memberId,
			EventMapper.toMemberProfileUpdatedEvent(member, now)
		);
	}
}
