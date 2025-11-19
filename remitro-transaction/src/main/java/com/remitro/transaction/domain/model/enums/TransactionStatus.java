package com.remitro.transaction.domain.model.enums;

/**
 * 거래가 "지금 어떤 상태인지" 표현
 * PENDING -> 아직 최종 확정되지 않음
 * COMPLETED -> 정상 처리 완료
 * FAILED -> 실패
 */
public enum TransactionStatus {

	PENDING,
	COMPLETED,
	FAILED
}
