package com.remitro.event.domain.member.model;

import java.time.LocalDateTime;

import com.remitro.event.domain.member.enums.MemberLifecycleStatus;

public record MemberStatusChangedEvent(
	Long memberId,

	MemberLifecycleStatus memberStatus,

	String reason,

	LocalDateTime changedAt
) {
}
