package com.remitro.transaction.domain.enums;

public enum SuspiciousStatus {

	OPEN,				// 의심 거래가 탐지됨 (조치 전)
	REVIEWED,			// 시스템/운영자가 검토함
	CLOSED				// 정상 또는 제재 확정으로 종료
}
