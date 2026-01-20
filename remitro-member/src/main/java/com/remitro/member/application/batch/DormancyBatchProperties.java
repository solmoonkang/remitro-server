package com.remitro.member.application.batch;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "batch.dormancy")
public record DormancyBatchProperties(
	int size,

	int inactivityYears
) {
}
