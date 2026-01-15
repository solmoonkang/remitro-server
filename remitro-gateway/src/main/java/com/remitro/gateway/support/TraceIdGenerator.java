package com.remitro.gateway.support;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class TraceIdGenerator {

	public String generate() {
		return UUID.randomUUID().toString();
	}
}
