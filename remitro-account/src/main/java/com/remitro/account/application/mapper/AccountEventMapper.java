package com.remitro.account.application.mapper;

import java.time.LocalDateTime;

import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.enums.AccountStatus;
import com.remitro.common.contract.account.AccountDepositEvent;
import com.remitro.common.contract.account.AccountStatusChangedEvent;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountEventMapper {

	public static AccountStatusChangedEvent toAccountStatusChangedEvent(Account account, AccountStatus accountStatus) {
		return new AccountStatusChangedEvent(
			account.getId(),
			account.getMemberId(),
			accountStatus.name(),
			account.getAccountStatus().name(),
			LocalDateTime.now()
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
