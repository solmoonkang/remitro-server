package com.remitro.member.domain.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus {

	ACTIVE("정상"),
	LOCKED("잠금"),
	DORMANT("휴면"),
	SUSPENDED("정지"),
	WITHDRAWN("탈퇴");

	private final String description;
}
