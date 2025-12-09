package com.remitro.transaction.infrastructure.messaging.dispatcher;

import static com.remitro.transaction.infrastructure.constant.AccountEventType.*;

import org.springframework.stereotype.Component;

import com.remitro.common.contract.event.EventEnvelope;
import com.remitro.common.util.JsonMapper;
import com.remitro.transaction.infrastructure.messaging.handler.DepositEventHandler;
import com.remitro.transaction.infrastructure.messaging.handler.StatusUpdatedEventHandler;
import com.remitro.transaction.domain.event.AccountDepositEvent;
import com.remitro.transaction.domain.event.AccountStatusUpdatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionEventDispatcher {

	private final DepositEventHandler depositEventHandler;
	private final StatusUpdatedEventHandler statusUpdatedEventHandler;

	public void dispatch(EventEnvelope eventEnvelope) {
		switch (eventEnvelope.eventType()) {
			case ACCOUNT_DEPOSIT -> depositEventHandler.handle(
				eventEnvelope.eventId(),
				JsonMapper.fromJSON(eventEnvelope.eventPayload(), AccountDepositEvent.class)
			);

			case ACCOUNT_STATUS_UPDATED -> statusUpdatedEventHandler.handle(
				eventEnvelope.eventId(),
				JsonMapper.fromJSON(eventEnvelope.eventPayload(), AccountStatusUpdatedEvent.class)
			);

			default -> log.warn("[✅ LOGGER] 알 수 없는 MEMBER 이벤트 타입을 수신했습니다: EVENT_TYPE={}",
				eventEnvelope.eventType()
			);
		}
	}
}
