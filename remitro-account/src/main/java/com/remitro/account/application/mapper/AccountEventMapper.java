package com.remitro.account.application.mapper;

import java.time.LocalDateTime;

import com.remitro.account.domain.enums.AccountStatus;
import com.remitro.account.domain.enums.AccountStatusUpdateReason;
import com.remitro.account.domain.enums.ActorType;
import com.remitro.account.domain.event.account.AccountDepositEvent;
import com.remitro.account.domain.event.account.AccountOpenedEvent;
import com.remitro.account.domain.event.account.AccountStatusUpdatedEvent;
import com.remitro.account.domain.model.Account;
import com.remitro.account.infrastructure.constant.EventSchema;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountEventMapper {

	public static AccountOpenedEvent toAccountOpenedEvent(
		Account account,
		ActorType actorType,
		AccountStatusUpdateReason accountStatusUpdateReason
	) {
		return new AccountOpenedEvent(
			account.getId(),
			account.getMemberId(),
			account.getAccountType().getCode(),
			actorType.name(),
			accountStatusUpdateReason.name(),
			LocalDateTime.now(),
			EventSchema.ACCOUNT_OPENED_V1
		);
	}

	public static AccountStatusUpdatedEvent toAccountStatusUpdatedEvent(
		Account account,
		AccountStatus previousStatus,
		ActorType actorType,
		AccountStatusUpdateReason accountStatusUpdateReason
	) {
		return new AccountStatusUpdatedEvent(
			account.getId(),
			account.getMemberId(),
			previousStatus.name(),
			account.getAccountStatus().name(),
			actorType.name(),
			accountStatusUpdateReason.name(),
			LocalDateTime.now(),
			EventSchema.ACCOUNT_STATUS_UPDATED_V1
		);
	}

	public static AccountDepositEvent toAccountDepositEvent(Account account, Long amount, String description) {
		return new AccountDepositEvent(
			account.getId(),
			account.getMemberId(),
			amount,
			account.getBalance(),
			description,
			LocalDateTime.now()
		);
	}
}
