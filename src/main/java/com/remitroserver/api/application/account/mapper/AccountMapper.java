package com.remitroserver.api.application.account.mapper;

import static com.remitroserver.global.error.model.ErrorMessage.*;

import com.remitroserver.api.domain.account.entity.Account;
import com.remitroserver.api.dto.account.response.AccountSummaryResponse;

public class AccountMapper {

	private AccountMapper() {
		throw new UnsupportedOperationException(UTILITY_CLASS_INSTANTIATION_ERROR.getMessage());
	}

	public static AccountSummaryResponse toSummaryResponse(Account account) {
		return AccountSummaryResponse.builder()
			.accountNumber(account.getAccountNumber())
			.balance(account.getBalance().getValue())
			.status(account.getStatus())
			.createdAt(account.getCreatedAt())
			.build();
	}
}
