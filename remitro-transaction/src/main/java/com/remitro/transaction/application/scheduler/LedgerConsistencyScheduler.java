package com.remitro.transaction.application.scheduler;

import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.remitro.transaction.application.validator.LedgerConsistencyChecker;
import com.remitro.transaction.domain.repository.LedgerEntryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LedgerConsistencyScheduler {

	private final LedgerEntryRepository ledgerEntryRepository;
	private final LedgerConsistencyChecker ledgerConsistencyChecker;

	@Scheduled(cron = "0 */10 * * * *")
	public void validateAllAccountLedgers() {
		final Set<Long> accountIds = ledgerEntryRepository.findDistinctAccountIds();

		for (Long accountId : accountIds) {
			try {
				ledgerConsistencyChecker.validateLedgerConsistency(accountId);

			} catch (Exception e) {
				log.error("[✅ LOGGER] LEDGER ENTRY 정합성 오류가 발생했습니다: ACCOUNT_ID={}, ERROR_MESSAGE={}",
					accountId,
					e.getMessage(),
					e
				);
			}
		}
	}
}
