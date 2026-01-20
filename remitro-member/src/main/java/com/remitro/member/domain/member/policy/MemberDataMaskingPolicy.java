package com.remitro.member.domain.member.policy;

import org.springframework.stereotype.Component;

@Component
public class MemberDataMaskingPolicy {

	public String maskEmail(String email) {
		final String[] parts = email.split("@");
		return parts[0].substring(0, Math.min(parts[0].length(), 3)) + "***@" + parts[1];
	}

	public String maskPhoneNumber(String phoneNumber) {
		return phoneNumber.replaceFirst("(\\d{3})(\\d{3,4})(\\d{4})", "$1-****-$3");
	}
}
