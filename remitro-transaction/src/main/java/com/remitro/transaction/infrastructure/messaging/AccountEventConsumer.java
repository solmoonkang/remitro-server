package com.remitro.transaction.infrastructure.messaging;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.remitro.common.contract.account.AccountDepositEvent;
import com.remitro.common.contract.account.AccountTransferEvent;
import com.remitro.common.contract.account.AccountWithdrawEvent;
import com.remitro.common.domain.enums.EventType;
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
		topics = "${topics.account-events}",
		groupId = "transaction-service"
	)
	public void handleAccountEvent(ConsumerRecord<String, String> consumerRecord) {
		final String accountEventPayload = consumerRecord.value();

		Header eventIdHeader = consumerRecord.headers().lastHeader("eventId");
		Header eventTypeHeader = consumerRecord.headers().lastHeader("eventType");

		if (eventIdHeader == null || eventTypeHeader == null) {
			log.error("[✅ LOGGER] 필수 헤더가 누락된 메시지를 수신했습니다. "
					+ "EVENT ID HEADER = {}, EVENT TYPE HEADER = {}, PAYLOAD = {}",
				eventIdHeader,
				eventTypeHeader,
				accountEventPayload
			);
			return;
		}

		final String eventId = new String(eventIdHeader.value(), StandardCharsets.UTF_8);
		final EventType eventType = EventType.valueOf(new String(eventTypeHeader.value(), StandardCharsets.UTF_8));

		try {
			switch (eventType) {
				case DEPOSIT -> handleDeposit(eventId, accountEventPayload);
				case WITHDRAWAL -> handleWithdraw(eventId, accountEventPayload);
				case TRANSFER -> handleTransfer(eventId, accountEventPayload);
				default -> log.warn("[✅ LOGGER] 처리 대상이 아닌 EVENT TYPE ={} 입니다. ", eventType.name());
			}
		} catch (Exception e) {
			log.error("[✅ LOGGER] 사용자 상태 변경 이벤트를 처리하지 못했습니다. "
					+ "EVENT ID = {}, PAYLOAD = {}",
				eventId,
				accountEventPayload
			);
			throw e;
		}
	}

	private void handleDeposit(String eventId, String accountEventPayload) {
		final AccountDepositEvent accountDepositEvent = JsonUtil.fromJSON(
			accountEventPayload,
			AccountDepositEvent.class
		);
		transactionService.recordDepositTransaction(eventId, accountDepositEvent);
	}

	private void handleWithdraw(String eventId, String accountEventPayload) {
		final AccountWithdrawEvent accountWithdrawEvent = JsonUtil.fromJSON(
			accountEventPayload,
			AccountWithdrawEvent.class
		);
		transactionService.recordWithdrawTransaction(eventId, accountWithdrawEvent);
	}

	private void handleTransfer(String eventId, String accountEventPayload) {
		final AccountTransferEvent accountTransferEvent = JsonUtil.fromJSON(
			accountEventPayload,
			AccountTransferEvent.class
		);
		transactionService.recordTransferTransaction(eventId, accountTransferEvent);
	}
}
