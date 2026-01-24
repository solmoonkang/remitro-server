package com.remitro.member.application.batch.dormancy;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "batch.dormancy")
public record DormancyBatchProperties(
	int chunkSize,

	int inactivityYears
) {
}
