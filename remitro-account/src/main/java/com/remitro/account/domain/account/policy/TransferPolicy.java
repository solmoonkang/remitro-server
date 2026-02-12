package com.remitro.account.domain.account.policy;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.enums.AccountStatus;
import com.remitro.account.domain.account.model.Account;

@Component
public class TransferPolicy {

	public boolean isSameAccount(Long fromAccountId, Long toAccountId) {
		return fromAccountId.equals(toAccountId);
	}

	public boolean isNotTransferable(Account account) {
		return account.getAccountStatus() != AccountStatus.ACTIVE;
	}

	public boolean isInsufficientBalance(Account account, Long amount) {
		return account.getBalance() < amount;
	}
}
