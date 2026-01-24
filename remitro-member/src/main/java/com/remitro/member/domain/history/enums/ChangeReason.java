package com.remitro.member.domain.history.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChangeReason {

	// 자동 처리 (SYSTEM)
	SYSTEM_LOCKED_BY_PASSWORD_FAILURE("비밀번호 5회 실패로 인한 자동 잠금"),
	SYSTEM_DORMANT_BY_INACTIVITY("장기 미접속으로 인한 자동 휴면 전환"),
	SYSTEM_UNLOCKED_BY_LOGIN_SUCCESS("잠금 기간 만료 후 정상 로그인에 따른 보안 잠금 해제"),
	SYSTEM_SUSPENDED_BY_ABNORMAL_ACTIVITY("이상 거래 및 부정 행위 탐지로 인한 자동 정지"),
	SYSTEM_ACTIVE_BY_SUSPENSION_EXPIRED("정지 기간 만료에 따른 자동 해제"),
	SYSTEM_WITHDRAWN_BY_POLICY("시스템 정책에 따른 자동 탈퇴"),

	// 관리자 처리 (ADMIN)
	ADMIN_SUSPENDED_BY_ABUSE("부정 사용 의심으로 인한 관리자 정지"),
	ADMIN_SUSPENDED_BY_REPORT("신고 접수로 인한 관리자 정지"),
	ADMIN_ACTIVE_BY_APPEAL("소명 확인 후 관리자 정상화"),
	ADMIN_WITHDRAWN_BY_FORCE("관리자에 의한 강제 탈퇴"),

	// 유저 처리 (USER)
	USER_WITHDRAWN_BY_SELF("사용자 본인 요청에 의한 탈퇴"),
	USER_ACTIVE_BY_DORMANT_RELEASE("본인 인증을 통한 휴면 해제");

	private final String description;
}
