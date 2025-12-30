package com.remitro.event.domain.account;

import java.time.LocalDateTime;

import com.remitro.event.domain.account.enums.AccountLifecycleStatus;
import com.remitro.event.domain.account.enums.AccountStatusChangeReason;

public record AccountStatusUpdateRequestedEvent(
	Long accountId,

	AccountLifecycleStatus targetStatus,

	AccountStatusChangeReason statusUpdateReason,

	LocalDateTime occurredAt
) {
}
