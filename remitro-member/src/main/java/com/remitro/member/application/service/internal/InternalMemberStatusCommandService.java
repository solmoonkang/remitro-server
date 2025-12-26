package com.remitro.member.application.service.internal;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.domain.model.Member;
import com.remitro.member.infrastructure.messaging.MemberEventPublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InternalMemberStatusCommandService {

	private final MemberFinder memberFinder;
	private final Clock clock;
	private final MemberEventPublisher memberEventPublisher;

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
