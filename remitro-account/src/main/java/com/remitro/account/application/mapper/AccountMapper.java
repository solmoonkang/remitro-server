package com.remitro.account.application.mapper;

import java.util.List;

import com.remitro.account.application.dto.response.AccountDetailResponse;
import com.remitro.account.application.dto.response.AccountSummaryResponse;
import com.remitro.account.application.dto.response.AccountsSummaryResponse;
import com.remitro.account.application.dto.response.OpenAccountCreationResponse;
import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.MemberProjection;

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

	public static AccountSummaryResponse toAccountSummaryResponse(Account account) {
		return new AccountSummaryResponse(
			account.getId(),
			account.getAccountNumber(),
			account.getAccountName(),
			account.getBalance(),
			account.getAccountType(),
			account.getAccountStatus()
		);
	}

	public static AccountsSummaryResponse toAccountsSummaryResponse(MemberProjection member, List<Account> accounts) {
		return new AccountsSummaryResponse(
			member.getMemberId(),
			member.getNickname(),
			accounts.stream()
				.map(AccountMapper::toAccountSummaryResponse)
				.toList()
		);
	}
}
