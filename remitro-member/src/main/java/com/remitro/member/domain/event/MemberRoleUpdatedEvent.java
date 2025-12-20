package com.remitro.member.domain.event;

import java.time.LocalDateTime;

import com.remitro.common.security.Role;

public record MemberRoleUpdatedEvent(
	Long memberId,

	Role previousRole,

	Role currentRole,

	Long updatedAdminId,

	LocalDateTime updatedAt
) {
}
