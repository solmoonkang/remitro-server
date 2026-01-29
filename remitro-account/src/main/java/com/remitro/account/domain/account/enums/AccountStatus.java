package com.remitro.account.domain.account.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountStatus {

	ACTIVE("활성"),
	SUSPENDED("중지"),
	CLOSED("해지");

	private final String description;
}
