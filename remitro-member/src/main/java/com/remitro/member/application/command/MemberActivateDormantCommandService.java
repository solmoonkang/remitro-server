package com.remitro.member.application.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.domain.member.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberActivateDormantCommandService {

	private final MemberFinder memberFinder;

	public void activate(Long memberId) {
		final Member member = memberFinder.getById(memberId);

		if (member.isDormant()) {
			member.unlock();
		}
	}
}
