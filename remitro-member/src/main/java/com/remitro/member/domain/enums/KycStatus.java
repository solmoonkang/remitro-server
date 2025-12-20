package com.remitro.member.domain.enums;

public enum KycStatus {

	UNVERIFIED,
	PENDING,
	VERIFIED,
	REJECTED;

	public boolean isFinalStatus() {
		return this == VERIFIED;
	}

	public boolean isAccountOpenAllowed() {
		return this == VERIFIED;
	}
}

