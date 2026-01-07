package com.remitro.member.application.command;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.event.MemberEventPublisher;
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.domain.member.model.ActivityStatus;
import com.remitro.member.domain.member.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberLoginFailureCommandService {

	private final MemberFinder memberFinder;

	private final MemberEventPublisher memberEventPublisher;
	private final Clock clock;

	public void handleLoginFailure(Long memberId) {
		final Member member = memberFinder.getById(memberId);
		member.increaseLoginFailure(LocalDateTime.now(clock));

		if (member.getActivityStatus() == ActivityStatus.LOCKED) {
			memberEventPublisher.publishMemberLocked(member);
		}
	}
}
