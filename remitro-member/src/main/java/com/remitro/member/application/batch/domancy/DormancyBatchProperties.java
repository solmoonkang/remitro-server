package com.remitro.member.application.batch.domancy;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "batch.dormancy")
public record DormancyBatchProperties(
	int chunkSize,

	int inactivityYears
) {
}
