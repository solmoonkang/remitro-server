package com.remitro.member.application.support;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.remitro.member.domain.model.KycVerification;

@Component
public class KycRetryPolicy {

	private static final int KYC_RETRY_COOLDOWN_DAYS = 30;

	public boolean isRetryAllowed(KycVerification latestVerification, Clock clock) {
		if (!latestVerification.isRejected()) return true;

		return LocalDateTime.now(clock)
			.isAfter(latestVerification.getRejectedAt().plusDays(KYC_RETRY_COOLDOWN_DAYS));
	}
}
