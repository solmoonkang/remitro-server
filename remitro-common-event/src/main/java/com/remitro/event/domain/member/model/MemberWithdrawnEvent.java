package com.remitro.event.domain.member.model;

import java.time.LocalDateTime;

import com.remitro.event.domain.member.enums.MemberLifecycleStatus;

public record MemberWithdrawnEvent(
	Long memberId,

	MemberLifecycleStatus memberStatus,

	LocalDateTime withdrawnAt
) {
}
