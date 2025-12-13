package com.remitro.account.domain.event.member;

import java.time.LocalDateTime;

public record MemberKycUpdatedEvent(
	Long memberId,

	String kycStatus,

	LocalDateTime verifiedAt,

	boolean isAccountOpenAllowed
) {
}
