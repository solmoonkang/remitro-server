package com.remitro.account.domain.account.policy;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.projection.enums.MemberProjectionStatus;
import com.remitro.account.domain.projection.model.MemberProjection;

@Component
public class OpenPolicy {

	public boolean isRestricted(MemberProjection member) {
		return member.getMemberStatus() != MemberProjectionStatus.ACTIVE;
	}
}
