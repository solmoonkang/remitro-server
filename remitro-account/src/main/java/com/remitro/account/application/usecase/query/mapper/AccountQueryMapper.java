package com.remitro.account.application.usecase.query.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.remitro.account.application.usecase.query.dto.response.AccountDetailResponse;
import com.remitro.account.application.usecase.query.dto.response.AccountSummaryResponse;
import com.remitro.account.domain.account.model.Account;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountQueryMapper {

	public static AccountSummaryResponse toAccountSummaryResponse(Account account) {
		return new AccountSummaryResponse(
			account.getId(),
			account.getAccountNumber(),
			account.getAccountName(),
			account.getProductType(),
			account.getLifecycleStatus(),
			account.getBalance()
		);
	}

	public static AccountDetailResponse toAccountDetailResponse(Account account, LocalDateTime now) {
		return new AccountDetailResponse(
			account.getId(),
			account.getAccountNumber(),
			account.getAccountName(),
			account.getProductType(),
			account.getLifecycleStatus(),
			account.getBalance(),
			now
		);
	}
}
