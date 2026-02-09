package com.remitro.member.domain.outbox.port;

import java.util.concurrent.CompletableFuture;

public interface EventProducer {

	CompletableFuture<Void> produce(String topic, String partitionKey, String payloadJson);
}
