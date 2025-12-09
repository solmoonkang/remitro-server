package com.remitro.transaction.application.validator;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.remitro.common.error.exception.InternalServerException;
import com.remitro.common.error.model.ErrorMessage;
import com.remitro.transaction.domain.model.LedgerEntry;
import com.remitro.transaction.domain.repository.LedgerEntryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LedgerConsistencyChecker {

	private final LedgerEntryRepository ledgerEntryRepository;

	public void validateLedgerConsistency(Long accountId) {
		final List<LedgerEntry> ledgerEntries = ledgerEntryRepository.findAllByAccountIdOrderByOccurredAtAsc(accountId);

		if (ledgerEntries.isEmpty())
			return;

		final long computeBalance = calculateLedgerBalance(ledgerEntries);
		final long lastRecordedBalance = getLastRecordedBalance(ledgerEntries);

		if (!Objects.equals(computeBalance, lastRecordedBalance)) {
			throw new InternalServerException(ErrorMessage.ACCOUNT_BALANCE_CORRUPTED,
				"ACCOUNT_ID=" + accountId +
					" COMPUTED_BALANCE=" + computeBalance +
					" LAST_RECORDED_BALANCE=" + lastRecordedBalance
			);
		}
	}

	private long calculateLedgerBalance(List<LedgerEntry> ledgerEntries) {
		return ledgerEntries.stream()
			.mapToLong(this::applyLedgerDirection)
			.sum();
	}

	private long getLastRecordedBalance(List<LedgerEntry> entries) {
		final LedgerEntry lastEntry = entries.get(entries.size() - 1);
		return lastEntry.getBalanceAfter();
	}

	private long applyLedgerDirection(LedgerEntry ledgerEntry) {
		return switch (ledgerEntry.getLedgerDirection()) {
			case CREDIT -> ledgerEntry.getAmount();
			case DEBIT -> -ledgerEntry.getAmount();
		};
	}
}
