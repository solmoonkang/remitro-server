package com.remitro.member.domain.kyc.policy;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.remitro.member.domain.kyc.model.KycVerification;

@Component
public class KycRetryPolicy {

	private static final int KYC_RETRY_COOLDOWN_DAYS = 30;

	public boolean isRetryAllowed(KycVerification latestVerification, Clock clock) {
		if (!latestVerification.isRejected()) return true;

		return LocalDateTime.now(clock)
			.isAfter(latestVerification.getRejectedAt().plusDays(KYC_RETRY_COOLDOWN_DAYS));
	}
}
