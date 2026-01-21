package com.remitro.member.domain.member.policy;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.remitro.member.domain.member.enums.LoginSecurityStatus;
import com.remitro.member.domain.member.model.Member;

@Component
public class MemberLoginPolicy {

	public boolean validateLoginable(Member member, LocalDateTime now) {
		if (member.getLoginSecurityStatus() == LoginSecurityStatus.LOCKED && member.isUnlockable(now)) {
			member.unlock();
			return true;
		}

		return false;
	}

	public void validateFailure(Member member, LocalDateTime now) {
		member.increaseFailedCount(now);
	}
}
