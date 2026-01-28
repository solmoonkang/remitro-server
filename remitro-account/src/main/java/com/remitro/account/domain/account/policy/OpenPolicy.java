package com.remitro.account.domain.account.policy;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.enums.AccountType;
import com.remitro.account.domain.projection.model.MemberProjection;

@Component
public class OpenPolicy {

	public boolean isRestricted(MemberProjection member) {
		return !member.isActive();
	}

	public boolean isNotOpenable(AccountType accountType) {
		return !accountType.isOpenable();
	}
}
