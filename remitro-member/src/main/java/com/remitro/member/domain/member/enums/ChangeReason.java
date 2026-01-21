package com.remitro.member.domain.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChangeReason {

	// 자동 처리 (SYSTEM)
	SYSTEM_LOCKED_BY_PASSWORD_FAILURE("비밀번호 5회 실패로 인한 자동 잠금"),
	SYSTEM_DORMANT_BY_INACTIVITY("장기 미접속으로 인한 자동 휴면 전환"),
	SYSTEM_UNLOCKED_BY_LOGIN_SUCCESS("잠금 해제 조건 충족 후 로그인 성공"),

	// 관리자 처리 (ADMIN)
	ADMIN_SUSPENDED_BY_ABUSE("부정 사용 의심으로 인한 관리자 정지"),
	ADMIN_SUSPENDED_BY_REPORT("신고 접수로 인한 관리자 정지"),
	ADMIN_ACTIVE_BY_APPEAL("소명 확인 후 관리자 정상화"),

	// 유저 처리 (USER)
	USER_WITHDRAWN_BY_SELF("사용자 본인 요청에 의한 탈퇴"),
	USER_ACTIVE_BY_DORMANT_RELEASE("본인 인증을 통한 휴면 해제");

	private final String description;
}
