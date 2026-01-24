package com.remitro.member.application.command;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.application.support.MemberStatusRecorder;
import com.remitro.member.domain.audit.enums.ChangeReason;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SystemMemberCommandService {

	private final MemberFinder memberFinder;
	private final MemberStatusRecorder memberStatusRecorder;
	private final Clock clock;

	public void suspend(Long memberId) {
		final LocalDateTime now = LocalDateTime.now(clock);

		final Member member = memberFinder.getMemberById(memberId);
		final MemberStatus previousStatus = member.getMemberStatus();

		// TODO: account 모듈에서 이상 거래 감지 이벤트를 보낼 때, 위반 강도에 따라 기간을 담아 보낸다.
		//  -> 1회 위반 7일, 2회 위반 30일, 중대 위반 무기한(null)
		member.suspendBySystem(now, null);

		memberStatusRecorder.recordSystemAction(
			member, previousStatus, ChangeReason.SYSTEM_SUSPENDED_BY_ABNORMAL_ACTIVITY
		);
	}
}
