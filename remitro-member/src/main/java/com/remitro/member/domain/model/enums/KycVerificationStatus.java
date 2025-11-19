package com.remitro.member.domain.model.enums;

/**
 * 회원 KYC 심사 상태
 * PENDING -> 심사 중
 * VERIFIED -> 승인
 * REJECTED -> 거절
 */
public enum KycVerificationStatus {

	PENDING,
	VERIFIED,
	REJECTED
}
