package com.remitro.account.application.mapper;

import java.util.List;

import com.remitro.account.application.dto.request.deposit.DepositCommand;
import com.remitro.account.application.dto.request.deposit.DepositRequest;
import com.remitro.account.application.dto.response.AccountBalanceResponse;
import com.remitro.account.application.dto.response.AccountDetailResponse;
import com.remitro.account.application.dto.response.AccountSummaryResponse;
import com.remitro.account.application.dto.response.AccountsSummaryResponse;
import com.remitro.account.application.dto.response.DepositResponse;
import com.remitro.account.application.dto.response.OpenAccountCreationResponse;
import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.MemberProjection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountMapper {

	public static OpenAccountCreationResponse toOpenAccountCreationResponse(Account account) {
		return new OpenAccountCreationResponse(
			account.getId(),
			account.getAccountNumber()
		);
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

	public static AccountBalanceResponse toAccountBalanceResponse(Long balance) {
		return new AccountBalanceResponse(
			balance
		);
	}

	public static DepositCommand toDepositCommand(
		Long memberId,
		String idempotencyKey,
		Long accountId,
		DepositRequest depositRequest
	) {
		return new DepositCommand(
			memberId,
			accountId,
			depositRequest.amount(),
			idempotencyKey,
			depositRequest.description()
		);
	}

	public static DepositResponse toDepositResponse(Account account, DepositCommand depositCommand) {
		return new DepositResponse(
			depositCommand.accountId(),
			depositCommand.amount(),
			account.getBalance()
		);
	}
}
