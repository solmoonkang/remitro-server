package com.remitro.account.application.usecase.open.mapper;

import com.remitro.account.application.usecase.open.dto.response.OpenAccountCreationResponse;
import com.remitro.account.domain.account.model.Account;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountOpenMapper {

	public static OpenAccountCreationResponse toOpenAccountCreationResponse(Account account) {
		return new OpenAccountCreationResponse(
			account.getId(),
			account.getAccountNumber()
		);
	}
}
