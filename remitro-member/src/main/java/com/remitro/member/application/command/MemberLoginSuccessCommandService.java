package com.remitro.member.application.command;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.domain.member.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberLoginSuccessCommandService {

	private final MemberFinder memberFinder;

	private final Clock clock;

	public void handleLoginSuccess(Long memberId) {
		final Member member = memberFinder.getById(memberId);
		member.resetLoginFailure();
		member.updateLastLoginAt(LocalDateTime.now(clock));
	}
}
