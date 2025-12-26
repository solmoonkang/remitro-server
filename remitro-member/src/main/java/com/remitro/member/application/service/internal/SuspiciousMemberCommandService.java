package com.remitro.member.application.service.internal;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.event.member.enums.LockActorType;
import com.remitro.member.infrastructure.messaging.MemberEventPublisher;
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.domain.enums.LockReason;
import com.remitro.member.domain.model.Member;

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
