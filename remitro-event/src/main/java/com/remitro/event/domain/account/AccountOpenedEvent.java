package com.remitro.event.domain.account;

import java.time.LocalDateTime;

import com.remitro.event.domain.account.enums.AccountActorType;
import com.remitro.event.domain.account.enums.AccountStatusChangeReason;
import com.remitro.event.domain.account.enums.AccountProductType;

public record AccountOpenedEvent(
	Long accountId,

	Long memberId,

	AccountProductType accountProductType,

	AccountActorType actorType,

	AccountStatusChangeReason statusUpdateReason,

	LocalDateTime occurredAt
) {
}
