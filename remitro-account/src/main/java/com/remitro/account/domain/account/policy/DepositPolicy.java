package com.remitro.account.domain.account.policy;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.enums.AccountStatus;
import com.remitro.account.domain.account.model.Account;

@Component
public class DepositPolicy {

	public boolean isNotDepositable(Account account) {
		return account.getAccountStatus() != AccountStatus.ACTIVE;
	}

	public boolean isInvalidAmount(Long amount) {
		return amount == null || amount <= 0;
	}
}
