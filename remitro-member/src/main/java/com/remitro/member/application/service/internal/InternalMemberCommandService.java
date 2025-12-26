package com.remitro.member.application.service.internal;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.service.member.MemberEventPublisher;
import com.remitro.member.application.service.member.MemberReadService;
import com.remitro.member.domain.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InternalMemberCommandService {

	private final MemberReadService memberReadService;
	private final MemberEventPublisher memberEventPublisher;
	private final Clock clock;

	@Transactional
	public void recordLoginSuccess(Long memberId) {
		final Member member = memberReadService.findMemberById(memberId);
		member.recordLoginSuccess(LocalDateTime.now(clock));
	}

	@Transactional
	public void recordLoginFailure(Long memberId) {
		final Member member = memberReadService.findMemberById(memberId);
		member.recordLoginFailure();
	}

	@Transactional
	public void unlockBySelfVerification(Long memberId) {
		final Member member = memberReadService.findMemberById(memberId);
		member.unlockBySelfVerification(LocalDateTime.now(clock));
		memberEventPublisher.publishMemberUnlockedBySelf(member, LocalDateTime.now(clock));
	}

	@Transactional
	public void markDormant(Long memberId) {
		final Member member = memberReadService.findMemberById(memberId);
		member.markDormant();
		memberEventPublisher.publishMemberDormant(member, LocalDateTime.now(clock));
	}

	@Transactional
	public void activateDormant(Long memberId) {
		final Member member = memberReadService.findMemberById(memberId);
		member.activateFromDormant(LocalDateTime.now(clock));
		memberEventPublisher.publishMemberActivated(member, LocalDateTime.now(clock));
	}
}
