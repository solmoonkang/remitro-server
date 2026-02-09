package com.remitro.event.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AggregateType {

	MEMBER("회원"),
	ACCOUNT("계좌"),
	TRANSACTION("거래");

	private final String description;
}
