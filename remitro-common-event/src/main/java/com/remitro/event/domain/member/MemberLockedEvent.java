package com.remitro.event.domain.member;

import com.remitro.event.domain.member.enums.MemberLockReason;

public record MemberLockedEvent(
	Long memberId,

	MemberLockReason lockReason
) {
}
