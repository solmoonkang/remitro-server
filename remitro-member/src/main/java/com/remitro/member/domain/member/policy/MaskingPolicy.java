package com.remitro.member.domain.member.policy;

import org.springframework.stereotype.Component;

@Component
public class MaskingPolicy {

	public String maskEmailForProfile(String email) {
		final String[] parts = email.split("@");
		return parts[0].substring(0, Math.min(parts[0].length(), 3))
			+ "***@" + parts[1];
	}

	public String maskPhoneNumberForProfile(String phoneNumber) {
		return phoneNumber.replaceFirst(
			"(\\d{3})(\\d{3,4})(\\d{4})",
			"$1-****-$3"
		);
	}

	public String maskPhoneNumberForWithdrawal(String phoneNumber) {
		return phoneNumber.replaceFirst(
			"(\\d{3})(\\d{3,4})(\\d{4})",
			"$1-****-****"
		);
	}
}
