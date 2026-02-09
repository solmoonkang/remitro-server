package com.remitro.account.application.mapper;

import java.time.LocalDateTime;

import com.remitro.account.application.command.dto.response.AccountOpenResponse;
import com.remitro.account.application.command.dto.response.DepositResponse;
import com.remitro.account.application.command.dto.response.WithdrawResponse;
import com.remitro.account.domain.ledger.model.AccountLedger;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class AccountMapper {

	public static AccountOpenResponse toAccountOpenResponse(
		String accountNumber,
		String accountAlias,
		LocalDateTime createdAt
	) {
		return new AccountOpenResponse(
			accountNumber,
			accountAlias,
			createdAt
		);
	}

	public static DepositResponse toDepositResponse(
		AccountLedger accountLedger,
		String accountNumber,
		String amount,
		String currentBalance
	) {
		return new DepositResponse(
			accountLedger.getId(),
			accountNumber,
			amount,
			currentBalance,
			accountLedger.getCreatedAt()
		);
	}

	public static WithdrawResponse toWithdrawResponse(
		AccountLedger accountLedger,
		String accountNumber,
		String amount,
		String currentBalance
	) {
		return new WithdrawResponse(
			accountLedger.getId(),
			accountNumber,
			amount,
			currentBalance,
			accountLedger.getCreatedAt()
		);
	}
}
