package com.remitro.transaction.domain.model.enums;

/**
 * 의심 거래 케이스 처리 상태
 * OPEN -> 새로 탐지됨, 미처리
 * REVIEWED -> 담당자가 검토 중 / 1차 검토 완료
 * CLOSED -> 최종 처리 완료
 */
public enum SuspiciousStatus {

	OPEN,
	REVIEWED,
	CLOSED
}
