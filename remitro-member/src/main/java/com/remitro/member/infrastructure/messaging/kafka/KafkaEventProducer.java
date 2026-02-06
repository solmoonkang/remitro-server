package com.remitro.member.infrastructure.messaging.kafka;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.remitro.member.domain.outbox.port.EventProducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventProducer implements EventProducer {

	private final KafkaTemplate<String, String> stringKafkaTemplate;

	@Override
	public CompletableFuture<Void> produce(String topic, String partitionKey, String payloadJson) {
		return stringKafkaTemplate.send(topic, partitionKey, payloadJson)
			.thenAccept(sendResult -> logSuccess(sendResult, topic, partitionKey))
			.exceptionally(exception -> {
				logFailure(exception, topic, partitionKey);
				throw new RuntimeException("[❎ ERROR] KAFKA 이벤트 발행에 실패했습니다.", exception);
			});
	}

	private void logSuccess(SendResult<String, String> sendResult, String topic, String partitionKey) {
		log.info("[✅ LOGGER] KAFKA 이벤트 발행에 성공했습니다. (TOPIC = {}, PARTITION_KEY = {}, OFFSET = {})",
			topic, partitionKey, sendResult.getRecordMetadata().offset()
		);
	}

	private void logFailure(Throwable throwable, String topic, String partitionKey) {
		log.error("[✅ LOGGER] KAFKA 이벤트 발행에 실패했습니다. (TOPIC = {}, PARTITION_KEY = {}, ERROR = {})",
			topic, partitionKey, throwable.getMessage()
		);
	}
}
