package com.remitro.member.application.event.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventContextProvider {

	@Value("${spring.application.name}")
	private String producer;

	public String producer() {
		return producer;
	}

	public String traceId() {
		return TraceContext.get();
	}
}
