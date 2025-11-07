package com.remitro.account.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {

	ACCOUNT_OPENED, DEPOSIT_EVENT, WITHDRAWAL_EVENT, TRANSFER_EVENT
}
