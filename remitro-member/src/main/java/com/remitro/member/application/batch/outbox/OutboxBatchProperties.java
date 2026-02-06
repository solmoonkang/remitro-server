package com.remitro.member.application.batch.outbox;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "batch.outbox")
public record OutboxBatchProperties(
	int chunkSize,

	long fixedDelay
) {
}
