package com.remitro.account.domain.ledger.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionType {

	DEPOSIT("입금"),
	WITHDRAW("출금"),
	TRANSFER_IN("이체 입금"),
	TRANSFER_OUT("이체 출금"),
	CANCEL("거래 취소");

	private final String description;
}
