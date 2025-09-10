package com.remitro.account.application.mapper;

import java.util.List;

import com.remitro.account.application.dto.response.AccountDetailResponse;
import com.remitro.account.domain.model.Account;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountMapper {

	public static AccountDetailResponse toAccountDetailResponse(Account account) {
		return new AccountDetailResponse(account.getMember().getNickname(), account.getAccountNumber(),
			account.getAccountName(), account.getBalance(), null, account.getAccountType().name());
	}

	public static List<AccountDetailResponse> toAccountListResponse(List<Account> accounts) {
		return accounts.stream()
			.map(AccountMapper::toAccountDetailResponse)
			.toList();
	}
}
