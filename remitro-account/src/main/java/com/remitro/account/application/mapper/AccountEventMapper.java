package com.remitro.account.application.mapper;

import java.time.LocalDateTime;

import com.remitro.account.domain.model.Account;
import com.remitro.common.contract.account.AccountDepositEvent;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountEventMapper {

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
