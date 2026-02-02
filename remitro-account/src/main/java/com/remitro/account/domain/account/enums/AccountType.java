package com.remitro.account.domain.account.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountType {

	SAVINGS("110", "보통예금", "입출금이 자유로운 일반 계좌", true, 1),
	CHECKING("120", "당좌예금", "수표 발행 등이 가능한 기업용 계좌", false, 0),
	DEPOSIT("130", "정기예금", "저축 목적의 예금 계좌", true, 3);

	private final String prefix;
	private final String name;
	private final String description;

	private final boolean openable;
	private final int maxAccountsPerMember;

	public String getDefaultAlias() {
		return String.format("%s 계좌", this.name);
	}
}
