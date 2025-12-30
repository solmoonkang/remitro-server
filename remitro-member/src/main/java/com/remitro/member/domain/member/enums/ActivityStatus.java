package com.remitro.member.domain.member.enums;

public enum ActivityStatus {

	ACTIVE,			// 정상 활동 가능
	DORMANT,		// 휴면 (로그인 가능, 거래 제한)
	LOCKED,			// 보안 잠금 (로그인 불가)
	WITHDRAWN;		// 탈퇴 (최종 상태)

	public boolean isTerminal() {
		return this == WITHDRAWN;
	}
}
