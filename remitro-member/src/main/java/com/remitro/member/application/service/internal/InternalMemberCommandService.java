package com.remitro.member.application.service.internal;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.event.member.enums.UnlockActorType;
import com.remitro.member.infrastructure.messaging.MemberEventPublisher;
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.domain.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InternalMemberCommandService {

	private final MemberFinder memberFinder;
	private final MemberEventPublisher memberEventPublisher;
	private final Clock clock;

	@Transactional
	public void recordLoginSuccess(Long memberId) {
		final Member member = memberFinder.getById(memberId);
		member.recordLoginSuccess(LocalDateTime.now(clock));
	}

	@Transactional
	public void recordLoginFailure(Long memberId) {
		final Member member = memberFinder.getById(memberId);
		member.recordLoginFailure();
	}

	@Transactional
	public void unlockBySelfVerification(Long memberId) {
		final Member member = memberFinder.getById(memberId);
		member.unlockBySelfVerification(LocalDateTime.now(clock));

		memberEventPublisher.publishMemberUnlocked(member, null, UnlockActorType.SELF, LocalDateTime.now(clock));
	}

	@Transactional
	public void markDormant(Long memberId) {
		final Member member = memberFinder.getById(memberId);
		member.markDormant();

		memberEventPublisher.publishMemberDormant(member, LocalDateTime.now(clock));
	}

	@Transactional
	public void activateDormant(Long memberId) {
		final Member member = memberFinder.getById(memberId);
		member.activateFromDormant(LocalDateTime.now(clock));

		memberEventPublisher.publishMemberActivated(member, LocalDateTime.now(clock));
	}
}
