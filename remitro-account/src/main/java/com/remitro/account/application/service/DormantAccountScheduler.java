package com.remitro.account.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.enums.AccountStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DormantAccountScheduler {

	private final AccountReadService accountReadService;
	private final AccountOutboxService accountOutboxService;

	@Scheduled(cron = "0 0 3 * * *")
	@Transactional
	public void markDormantAccounts() {
		LocalDateTime changedDormantAccountTime = LocalDateTime.now().minusYears(1);

		final List<Account> targetAccounts = accountReadService.findInactiveAccounts(changedDormantAccountTime);

		for (Account account : targetAccounts) {
			AccountStatus previousStatus = account.getAccountStatus();
			account.dormant();
			accountOutboxService.appendStatusChangedEvent(account, previousStatus);
		}

		log.info("[✅ LOGGER] 휴면 전환 처리 완료, (COUNT = {})",
			targetAccounts.size()
		);
	}
}
