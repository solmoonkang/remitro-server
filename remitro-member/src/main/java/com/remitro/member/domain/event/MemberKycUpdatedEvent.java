package com.remitro.member.domain.event;

import java.time.LocalDateTime;

import com.remitro.member.domain.enums.KycStatus;

public record MemberKycUpdatedEvent(
	Long memberId,

	KycStatus kycStatus,

	LocalDateTime verifiedAt,

	boolean isMemberActive
) {
}
