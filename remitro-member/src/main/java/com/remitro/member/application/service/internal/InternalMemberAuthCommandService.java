package com.remitro.member.application.service.internal;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.event.member.enums.UnlockActorType;
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.domain.model.Member;
import com.remitro.member.infrastructure.messaging.MemberEventPublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InternalMemberAuthCommandService {

	private final MemberFinder memberFinder;
	private final Clock clock;
	private final MemberEventPublisher memberEventPublisher;

	@Transactional
	public void recordLoginSuccess(Long memberId) {
		final Member member = memberFinder.getById(memberId);
		member.recordLoginSuccess(LocalDateTime.now(clock));
	}

	@Transactional
	public void recordLoginFailure(Long memberId) {
		final Member member = memberFinder.getById(memberId);
		member.recordLoginFailure(LocalDateTime.now(clock));
	}

	@Transactional
	public void unlockBySelfVerification(Long memberId) {
		final Member member = memberFinder.getById(memberId);
		member.unlock(LocalDateTime.now(clock));

		memberEventPublisher.publishMemberUnlocked(member, null, UnlockActorType.SELF, LocalDateTime.now(clock));
	}
}
