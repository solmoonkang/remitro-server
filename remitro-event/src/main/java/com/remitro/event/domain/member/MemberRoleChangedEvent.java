package com.remitro.event.domain.member;

import java.time.LocalDateTime;

import com.remitro.common.security.Role;

public record MemberRoleChangedEvent(
	Long memberId,

	Role previousRole,

	Role currentRole,

	Long adminMemberId,

	LocalDateTime occurredAt
) {
}
