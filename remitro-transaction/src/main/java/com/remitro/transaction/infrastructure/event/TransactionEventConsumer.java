package com.remitro.transaction.infrastructure.event;

import static com.remitro.common.common.util.KafkaConstant.*;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.service.AccountReadService;
import com.remitro.common.common.event.TransactionEventMessage;
import com.remitro.common.common.util.JsonUtil;
import com.remitro.transaction.domain.model.enums.TransactionType;
import com.remitro.transaction.domain.service.TransactionService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransactionEventConsumer {

	private final AccountReadService accountReadService;
	private final TransactionService transactionService;

	@Transactional
	@KafkaListener(topics = TOPIC_TRANSACTION_EVENTS, groupId = GROUP_TRANSACTION_CONSUMERS)
	public void processTransactionMessage(String message) {
		final TransactionEventMessage eventMessage = JsonUtil.fromJSON(message, TransactionEventMessage.class);

		switch (TransactionType.valueOf(eventMessage.transactionType())) {
			case TRANSFER -> handleTransferEvent(eventMessage);
			case DEPOSIT -> handleDepositEvent(eventMessage);
			case WITHDRAWAL -> handleWithdrawalEvent(eventMessage);
			default -> throw new IllegalStateException("[❎ ERROR] UNEXPECTED VALUE = " + eventMessage.transactionType());
		}
	}

	private void handleTransferEvent(TransactionEventMessage eventMessage) {
		final Account sender = accountReadService.findAccountByNumber(eventMessage.senderAccountNumber());
		final Account receiver = accountReadService.findAccountByNumber(eventMessage.receiverAccountNumber());
		transactionService.recordTransferTransaction(
			sender, receiver, eventMessage.amount(), eventMessage.idempotencyKey());
	}

	private void handleDepositEvent(TransactionEventMessage eventMessage) {
		final Account receiver = accountReadService.findAccountByNumber(eventMessage.receiverAccountNumber());
		transactionService.recordDepositTransaction(receiver, eventMessage.amount(), eventMessage.idempotencyKey());
	}

	private void handleWithdrawalEvent(TransactionEventMessage eventMessage) {
		final Account sender = accountReadService.findAccountByNumber(eventMessage.senderAccountNumber());
		transactionService.recordWithdrawalTransaction(sender, eventMessage.amount(), eventMessage.idempotencyKey());
	}
}
