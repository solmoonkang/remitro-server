package com.remitro.account.application.mapper;

import com.remitro.account.application.dto.response.AccountDetailResponse;
import com.remitro.account.application.dto.response.OpenAccountCreationResponse;
import com.remitro.account.domain.model.Account;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountMapper {

	public static OpenAccountCreationResponse toOpenAccountCreationResponse(Account account) {
		return new OpenAccountCreationResponse(account.getId(), account.getAccountNumber());
	}

	public static AccountDetailResponse toAccountDetailResponse(Account account) {
		return new AccountDetailResponse(
			account.getId(),
			account.getAccountNumber(),
			account.getAccountName(),
			account.getBalance(),
			account.getAccountType(),
			account.getAccountStatus(),
			account.getCreatedAt()
		);
	}
}
