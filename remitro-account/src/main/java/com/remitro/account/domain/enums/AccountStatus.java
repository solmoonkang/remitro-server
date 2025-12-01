package com.remitro.account.domain.enums;

public enum AccountStatus {

	NORMAL,					// 정상 계좌 (입출금 모두 가능)

	FROZEN,					// 지급 정지 (출금/송금 불가, 입금만 허용 or 완전 차단은 은행 정책에 따라 바뀜)
	SUSPENDED,				// 이용 정지 (입출금 모두 차단 -> 금융사기 or 이상거래 탐지 시)

	DORMANT,				// 휴면 계좌 (입금 가능, 출금/송금 불가)
	INACTIVE,				// 미사용 (장기 미거래) 계좌 (제한 정도 다름)

	TERMINATED,				// 해지 완료 (입출금 불가, 조회만 가능)
	PENDING_TERMINATION		// 해지 처리중 (거래 불가, 일정 시간 뒤 TERMINATED로 변경)
}
