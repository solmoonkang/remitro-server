package com.remitro.event.domain.account;

import java.time.LocalDateTime;

import com.remitro.event.domain.account.enums.AccountActorType;
import com.remitro.event.domain.account.enums.AccountLifecycleStatus;
import com.remitro.event.domain.account.enums.AccountStatusChangeReason;

public record AccountStatusUpdatedEvent(
	Long accountId,

	Long memberId,

	AccountLifecycleStatus previousStatus,

	AccountLifecycleStatus currentStatus,

	AccountActorType actorType,

	AccountStatusChangeReason statusUpdateReason,

	LocalDateTime occurredAt
) {
}
