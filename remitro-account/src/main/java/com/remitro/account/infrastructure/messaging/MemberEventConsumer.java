package com.remitro.account.infrastructure.messaging;

import java.util.function.Consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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

	private static final TypeReference<EventEnvelope<JsonNode>> COMMON_ENVELOPE_TYPE =
		new TypeReference<>() {
		};
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

			final EventEnvelope<JsonNode> commonEnvelope = JsonSupport.fromJSON(
				objectMapper,
				payloadJson,
				COMMON_ENVELOPE_TYPE
			);

			this.dispatch(commonEnvelope, payloadJson);

		} catch (Exception e) {
			log.error("[✅ LOGGER] KAFKA CONSUMER 메시지 처리 중 오류가 발생했습니다. (PAYLOAD = {})", payloadJson, e);
		}
	}

	private void dispatch(EventEnvelope<JsonNode> commonEventEnvelope, String payloadJson) {
		final EventType eventType = commonEventEnvelope.eventType();

		switch (eventType) {
			case MEMBER_REGISTERED -> process(payloadJson, MEMBER_REGISTERED_TYPE, memberEventService::handleSignUp);
			default -> log.warn("[✅ LOGGER] 지원하지 않는 이벤트 타입입니다. (EVENT_TYPE = {})", eventType);
		}
	}

	private <T> void process(String payloadJson, TypeReference<EventEnvelope<T>> typeReference, Consumer<T> handler) {
		final EventEnvelope<T> eventEnvelope = JsonSupport.fromJSON(objectMapper, payloadJson, typeReference);
		handler.accept(eventEnvelope.payload());
	}
}
