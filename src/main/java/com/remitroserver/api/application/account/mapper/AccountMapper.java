package com.remitroserver.api.application.account.mapper;

import static com.remitroserver.global.error.model.ErrorMessage.*;

import com.remitroserver.api.domain.account.entity.Account;
import com.remitroserver.api.dto.account.response.AccountDetailResponse;
import com.remitroserver.api.dto.account.response.AccountSummaryResponse;

public class AccountMapper {

	private AccountMapper() {
		throw new UnsupportedOperationException(UTILITY_CLASS_INSTANTIATION_ERROR.getMessage());
	}

	public static AccountSummaryResponse toSummaryResponse(Account account) {
		return AccountSummaryResponse.builder()
			.accountNumber(account.getAccountNumber())
			.accountToken(account.getAccountToken())
			.balance(account.getBalance().getValue())
			.status(account.getStatus())
			.createdAt(account.getCreatedAt())
			.build();
	}

	public static AccountDetailResponse toDetailResponse(Account account) {
		return AccountDetailResponse.builder()
			.accountNumber(account.getAccountNumber())
			.accountType(account.getAccountType())
			.balance(account.getBalance().getValue())
			.status(account.getStatus())
			.createdAt(account.getCreatedAt())
			.ownerNickname(account.getMember().getNickname())
			.build();
	}
}
