package com.remitro.member.infrastructure.web;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class TraceIdProvider {

	public String currentTraceId() {
		return MDC.get("traceId");
	}
}
