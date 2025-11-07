package com.remitro.common.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AggregateType {

	ACCOUNT, TRANSACTION, MEMBER, REMITTANCE
}
