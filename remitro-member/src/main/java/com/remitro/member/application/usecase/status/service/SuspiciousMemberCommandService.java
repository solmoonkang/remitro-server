package com.remitro.member.application.usecase.status.service;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.event.domain.member.enums.LockActorType;
import com.remitro.member.infrastructure.messaging.MemberEventPublisher;
import com.remitro.member.application.common.support.MemberFinder;
import com.remitro.member.domain.member.enums.LockReason;
import com.remitro.member.domain.member.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SuspiciousMemberCommandService {

	private final MemberFinder memberFinder;
	private final MemberEventPublisher memberEventPublisher;
	private final Clock clock;

	@Transactional
	public void lockBySuspiciousActivity(Long memberId) {
		final Member member = memberFinder.getById(memberId);
		member.lockBySuspiciousActivity(LocalDateTime.now(clock));

		memberEventPublisher.publishMemberLocked(
			member,
			null,
			LockActorType.SYSTEM,
			LockReason.SUSPICIOUS_ACTIVITY,
			LocalDateTime.now(clock)
		);
	}
}
