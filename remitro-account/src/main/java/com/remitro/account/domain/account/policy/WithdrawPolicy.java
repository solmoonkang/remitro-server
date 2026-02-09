package com.remitro.account.domain.account.policy;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.enums.AccountStatus;
import com.remitro.account.domain.account.model.Account;

@Component
public class WithdrawPolicy {

	private static final long MINIMUM_TRANSACTION_AMOUNT = 1L;

	public boolean isNotWithdrawable(Account account) {
		return account.getAccountStatus() != AccountStatus.ACTIVE;
	}

	public boolean isInsufficientBalance(Account account, Long amount) {
		return account.getBalance() < amount;
	}

	public boolean isInvalidAmount(Long amount) {
		return amount == null || amount < MINIMUM_TRANSACTION_AMOUNT;
	}
}
