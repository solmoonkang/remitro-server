package com.remitro.member.application.batch.suspension;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "batch.suspension")
public record SuspensionBatchProperties(
	int chunkSize
) {
}
