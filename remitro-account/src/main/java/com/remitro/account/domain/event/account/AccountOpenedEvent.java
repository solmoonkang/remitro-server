package com.remitro.account.domain.event.account;

import java.time.LocalDateTime;

public record AccountOpenedEvent(
	Long accountId,

	Long memberId,

	String accountType,

	String actorType,

	String reasonCode,

	LocalDateTime occurredAt,

	int schemaVersion
) {
}
