package com.remitro.account.application.mapper;

import com.remitro.account.domain.model.Account;
import com.remitro.common.contract.account.AccountDepositEvent;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountEventMapper {

	public static AccountDepositEvent toAccountDepositEvent(Account account, Long amount) {
		return new AccountDepositEvent(
			account.getId(),
			account.getMemberId(),
			amount,
			account.getCreatedAt()
		);
	}
}
