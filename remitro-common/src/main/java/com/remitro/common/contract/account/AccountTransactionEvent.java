package com.remitro.common.contract.account;

import java.time.LocalDateTime;

public interface AccountTransactionEvent {

	Long accountId();

	Long memberId();

	Long amount();

	String description();

	LocalDateTime occurredAt();
}
