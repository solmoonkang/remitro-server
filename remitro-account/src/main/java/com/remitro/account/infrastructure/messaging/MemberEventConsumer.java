package com.remitro.account.infrastructure.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.remitro.account.application.projection.MemberEventService;
import com.remitro.event.common.EventEnvelope;
import com.remitro.event.common.EventType;
import com.remitro.event.domain.member.model.MemberRegisteredEvent;
import com.remitro.support.util.JsonSupport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventConsumer {

	private static final TypeReference<EventEnvelope<MemberRegisteredEvent>> MEMBER_REGISTERED_TYPE =
		new TypeReference<>() {
		};

	private final MemberEventService memberEventService;
	private final ObjectMapper objectMapper;

	@KafkaListener(
		topics = "${spring.kafka.topics.member-events}",
		groupId = "${spring.kafka.consumer.group-id}",
		containerFactory = "kafkaListenerContainerFactory"
	)
	public void consume(String payloadJson) {
		try {
			log.debug("[✅ LOGGER] KAFKA 메시지를 수신했습니다. (PAYLOAD = {})", payloadJson);

			final EventEnvelope<MemberRegisteredEvent> eventEnvelope = JsonSupport.fromJSON(
				objectMapper,
				payloadJson,
				MEMBER_REGISTERED_TYPE
			);

			if (eventEnvelope.eventType() != EventType.MEMBER_REGISTERED) {
				log.warn("[✅ LOGGER] 지원하지 않는 이벤트 타입입니다. (EVENT_TYPE = {})", eventEnvelope.eventType());
				return;
			}

			memberEventService.handleMemberRegistered(eventEnvelope.payload());

		} catch (Exception e) {
			log.error("[✅ LOGGER] KAFKA CONSUMER 메시지 처리 중 오류가 발생했습니다. (PAYLOAD = {})", payloadJson, e);
		}
	}
}
