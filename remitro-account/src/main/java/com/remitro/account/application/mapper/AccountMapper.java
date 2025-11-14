package com.remitro.account.application.mapper;

import com.remitro.account.application.dto.response.OpenAccountCreationResponse;
import com.remitro.account.domain.model.Account;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountMapper {

	public static OpenAccountCreationResponse toOpenAccountCreationResponse(Account account) {
		return new OpenAccountCreationResponse(account.getId(), account.getAccountNumber());
	}
}
