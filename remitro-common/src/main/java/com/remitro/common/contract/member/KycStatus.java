package com.remitro.common.contract.member;

/**
 * 회원의 KYC 인증 상태(AML/법규 기반 계정 사용 제한 여부)를 나타내는 도메인 상태
 * UNVERIFIED -> 기본 가입 상태 (KYC 제출 안 함)
 * PENDING -> KYC 제출 완료 후 검증 중
 * VERIFIED -> KYC 통과 (모든 금융 기능 사용 가능)
 * REJECTED -> KYC 실패 (정상 금융 기능 제한됨)
 */
public enum KycStatus {

	UNVERIFIED,
	PENDING,
	VERIFIED,
	REJECTED
}

