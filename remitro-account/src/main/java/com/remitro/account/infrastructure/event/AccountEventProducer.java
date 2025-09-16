package com.remitro.account.infrastructure.event;

import static com.remitro.common.common.util.KafkaConstant.*;
import static com.remitro.common.common.util.TransactionConstant.*;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.remitro.common.common.event.TransactionEventMessage;
import com.remitro.common.common.util.JsonUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountEventProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;

	public void publishTransferEvent(String sender, String receiver, Long amount) {
		final TransactionEventMessage eventMessage = toTransactionEventMessage(sender, receiver, amount, TRANSFER);
		publishTransactionEvent(eventMessage);
	}

	public void publishDepositEvent(String receiver, Long amount) {
		final TransactionEventMessage eventMessage = toTransactionEventMessage(null, receiver, amount, DEPOSIT);
		publishTransactionEvent(eventMessage);
	}

	public void publishWithdrawalEvent(String sender, Long amount) {
		final TransactionEventMessage eventMessage = toTransactionEventMessage(sender, null, amount, WITHDRAWAL);
		publishTransactionEvent(eventMessage);
	}

	private void publishTransactionEvent(TransactionEventMessage eventMessage) {
		final String JSONMessage = JsonUtil.toJSON(eventMessage);
		kafkaTemplate.send(TOPIC_TRANSACTION_EVENTS, JSONMessage);
	}

	private TransactionEventMessage toTransactionEventMessage(String sender, String receiver, Long amount,
		String transactionType) {

		return new TransactionEventMessage(sender, receiver, amount, transactionType);
	}
}
