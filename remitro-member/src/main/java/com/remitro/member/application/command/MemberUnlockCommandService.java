package com.remitro.member.application.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.event.MemberEventPublisher;
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.domain.member.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberUnlockCommandService {

	private final MemberFinder memberFinder;

	private final MemberEventPublisher memberEventPublisher;

	public void unlock(Long memberId) {
		final Member member = memberFinder.getById(memberId);

		member.unlock();

		memberEventPublisher.publishMemberUnlocked(member);
	}
}
