package com.remitro.event.domain.member.model;

import java.time.LocalDateTime;

import com.remitro.event.domain.member.enums.MemberLifecycleStatus;

public record MemberRegisteredEvent(
	Long memberId,

	String nickname,

	MemberLifecycleStatus memberStatus,

	LocalDateTime registeredAt
) {
}
