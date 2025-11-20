package com.remitro.transaction.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.contract.account.AccountDepositEvent;
import com.remitro.common.contract.account.AccountTransferEvent;
import com.remitro.common.contract.account.AccountWithdrawEvent;
import com.remitro.transaction.domain.model.Transaction;
import com.remitro.transaction.domain.model.enums.LedgerDirection;
import com.remitro.transaction.domain.model.enums.TransactionType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

	private final TransactionReadService transactionReadService;
	private final TransactionWriteService transactionWriteService;

	@Transactional
	public void recordDepositTransaction(String eventId, AccountDepositEvent accountDepositEvent) {
		if (transactionReadService.existsByEventId(eventId)) {
			return;
		}

		final Transaction transaction = transactionWriteService.saveTransaction(
			eventId,
			TransactionType.DEPOSIT,
			accountDepositEvent
		);
		transactionWriteService.saveLedgerEntry(
			transaction,
			LedgerDirection.CREDIT,
			accountDepositEvent.balanceAfter(),
			accountDepositEvent
		);
	}

	public void recordWithdrawTransaction(String eventId, AccountWithdrawEvent accountWithdrawEvent) {

	}

	public void recordTransferTransaction(String eventId, AccountTransferEvent accountTransferEvent) {

	}
}
