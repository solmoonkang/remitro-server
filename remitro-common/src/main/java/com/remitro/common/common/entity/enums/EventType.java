package com.remitro.common.common.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {

	DEPOSIT_EVENT("입금 이벤트"),
	WITHDRAWAL_EVENT("출금 이벤트"),
	TRANSFER_EVENT("송금 이벤트");

	private final String description;
}
