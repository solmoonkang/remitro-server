package com.remitro.account.application.service.account;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.service.outbox.AccountOutboxService;
import com.remitro.account.application.validator.AccountStatusTransactionValidator;
import com.remitro.account.domain.enums.AccountStatus;
import com.remitro.account.domain.model.Account;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DormantAccountService {

	private final AccountReadService accountReadService;
	private final AccountOutboxService accountOutboxService;
	private final AccountStatusTransactionValidator accountStatusTransactionValidator;

	@Transactional
	public int markDormantAccounts() {
		LocalDateTime threshold = LocalDateTime.now().minusYears(1);

		List<Account> accounts = accountReadService.findInactiveAccounts(threshold);

		for (Account account : accounts) {
			AccountStatus currentStatus = account.getAccountStatus();
			accountStatusTransactionValidator.validateStatusTransition(currentStatus, AccountStatus.DORMANT);
			account.applyAccountStatus(AccountStatus.DORMANT);
			accountOutboxService.appendAccountStatusUpdatedEvent(account, currentStatus);
		}

		return accounts.size();
	}
}
