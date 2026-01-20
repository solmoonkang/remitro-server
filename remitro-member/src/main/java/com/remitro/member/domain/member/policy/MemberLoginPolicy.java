package com.remitro.member.domain.member.policy;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;

@Component
public class MemberLoginPolicy {

	public void validateLoginable(Member member, LocalDateTime now) {
		if (member.getMemberStatus() == MemberStatus.LOCKED && member.isUnlockable(now)) {
			member.unlock();
		}
	}

	public void validateFailure(Member member, LocalDateTime now) {
		member.increaseFailedCount(now);
	}
}
