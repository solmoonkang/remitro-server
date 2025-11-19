package com.remitro.common.domain.enums;

/**
 * OutboxMessage에서 이 이벤트가 어느 도메인 루트(Aggregate)에서 나왔는지 표시하는 타입
 * MEMBER -> member 모듈
 * ACCOUNT -> account 모듈
 * TRANSACTION -> transaction 모듈
 * TRANSFER -> 나중에 송금 전용 모듈이 생길 경우
 */
public enum AggregateType {

	MEMBER,
	ACCOUNT,
	TRANSACTION,
	TANSFER
}
