package com.remitro.common.common.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AggregateType {

	ACCOUNT("계좌"),
	TRANSACTION("거래 내역"),
	MEMBER("회원");

	private final String description;
}
