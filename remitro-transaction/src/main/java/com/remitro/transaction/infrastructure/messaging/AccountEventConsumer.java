package com.remitro.transaction.infrastructure.messaging;

import static com.remitro.common.infra.util.KafkaConstant.*;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import com.remitro.common.contract.account.AccountDepositEvent;
import com.remitro.common.contract.account.AccountTransferEvent;
import com.remitro.common.contract.account.AccountWithdrawEvent;
import com.remitro.common.domain.enums.EventType;
import com.remitro.common.infra.error.exception.BadRequestException;
import com.remitro.common.infra.error.model.ErrorMessage;
import com.remitro.common.infra.util.JsonUtil;
import com.remitro.transaction.application.service.TransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountEventConsumer {

	private final TransactionService transactionService;

	@KafkaListener(
		topics = ACCOUNT_EVENTS_TOPIC_NAME,
		groupId = TRANSACTION_CONSUMER_GROUP_ID
	)
	@RetryableTopic(
		attempts = "5",
		dltTopicSuffix = "-dlq",
		backoff = @Backoff(
			delay = 1000,
			maxDelay = 16000,
			multiplier = 2
		)
	)
	public void handleAccountEvent(ConsumerRecord<String, String> consumerRecord) {
		final String accountEventPayload = consumerRecord.value();
		final String eventId = extractEventId(consumerRecord);
		final EventType eventType = extractEventType(consumerRecord);

		log.info("[✅ LOGGER] CONSUMER 이벤트 수신, (EVENT ID = {}, EVENT TYPE = {})",
			eventId,
			eventType
		);

		switch (eventType) {
			case DEPOSIT -> handleDeposit(eventId, accountEventPayload);
			case WITHDRAWAL -> handleWithdraw(eventId, accountEventPayload);
			case TRANSFER -> handleTransfer(eventId, accountEventPayload);
			default -> log.warn("[✅ LOGGER] 지원하지 않는 이벤트 타입, (EVENT TYPE = {})",
				eventType
			);
		}
	}

	// TODO: 규제 대응(RBA), 금융사기탐지(FDS), AML 대응이 가능하기 위해 프로덕션에 저장한다.
	@KafkaListener(topics = ACCOUNT_EVENTS_TOPIC_NAME)
	public void handleAccountStatusChangedEvent() {

	}

	private String extractEventId(ConsumerRecord<String, String> consumerRecord) {
		final Header eventIdHeader = consumerRecord.headers().lastHeader(EVENT_HEADER_ID);
		validateEventHeaders(eventIdHeader);
		return new String(eventIdHeader.value(), StandardCharsets.UTF_8);
	}

	private EventType extractEventType(ConsumerRecord<String, String> consumerRecord) {
		final Header eventTypeHeader = consumerRecord.headers().lastHeader(EVENT_HEADER_TYPE);
		validateEventHeaders(eventTypeHeader);
		return EventType.valueOf(new String(eventTypeHeader.value(), StandardCharsets.UTF_8));
	}

	private void handleDeposit(String eventId, String accountEventPayload) {
		final AccountDepositEvent depositEvent = JsonUtil.fromJSON(accountEventPayload, AccountDepositEvent.class);
		transactionService.recordDepositTransaction(eventId, depositEvent);
	}

	private void handleWithdraw(String eventId, String accountEventPayload) {
		final AccountWithdrawEvent withdrawEvent = JsonUtil.fromJSON(accountEventPayload, AccountWithdrawEvent.class);
		transactionService.recordWithdrawTransaction(eventId, withdrawEvent);
	}

	private void handleTransfer(String eventId, String accountEventPayload) {
		final AccountTransferEvent transferEvent = JsonUtil.fromJSON(accountEventPayload, AccountTransferEvent.class);
		transactionService.recordTransferTransaction(eventId, transferEvent);
	}

	private void validateEventHeaders(Header eventHeader) {
		if (eventHeader == null) {
			throw new BadRequestException(ErrorMessage.INVALID_EVENT_HEADER);
		}
	}
}
