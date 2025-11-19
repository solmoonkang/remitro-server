package com.remitro.common.contract.member;

/**
 * 회원 "활동 상태" 관리
 * ACTIVE -> 정상 활동
 * DORMANT -> 휴면 (장기 미접속, 금융 기능 일부 제한)
 * LOCKED -> 비밀번호 연속 실패 / 보안 이슈로 잠금
 * WITHDRAWN -> 회원 탈퇴 상태 (계정 사용 불가)
 */
public enum ActivityStatus {

	ACTIVE,
	DORMANT,
	LOCKED,
	WITHDRAWN
}
