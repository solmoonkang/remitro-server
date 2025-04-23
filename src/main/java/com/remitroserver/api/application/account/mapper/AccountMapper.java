package com.remitroserver.api.application.account.mapper;

import static com.remitroserver.global.error.model.ErrorMessage.*;

import java.time.LocalDateTime;
import java.util.List;

import com.remitroserver.api.domain.account.entity.Account;
import com.remitroserver.api.dto.account.response.AccountBalanceResponse;
import com.remitroserver.api.dto.account.response.AccountDetailResponse;
import com.remitroserver.api.dto.account.response.AccountSummaryResponse;
import com.remitroserver.api.dto.transaction.response.TransactionSummaryResponse;

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

	public static AccountDetailResponse toDetailResponse(
		Account account,
		List<TransactionSummaryResponse> transactionSummaryResponses) {

		return AccountDetailResponse.builder()
			.accountNumber(account.getAccountNumber())
			.accountType(account.getAccountType())
			.balance(account.getBalance().getValue())
			.status(account.getStatus())
			.createdAt(account.getCreatedAt())
			.ownerNickname(account.getMember().getNickname())
			.recentTransactions(transactionSummaryResponses)
			.build();
	}

	public static AccountBalanceResponse toBalanceResponse(Account account) {
		return AccountBalanceResponse.builder()
			.balance(account.getBalance().getValue())
			.retrievedAt(LocalDateTime.now())
			.build();
	}
}
