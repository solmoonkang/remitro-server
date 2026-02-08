package com.remitro.event.common;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.BadRequestException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {

	MEMBER_REGISTERED("member.registered", EventTopic.MEMBER_EVENTS, "1");

	private final String code;
	private final EventTopic eventTopic;
	private final String schemaVersion;

	@JsonValue
	public String jsonValue() {
		return code;
	}

	@JsonCreator
	public static EventType from(String code) {
		return Arrays.stream(values())
			.filter(value -> value.code.equals(code))
			.findFirst()
			.orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_INPUT_VALUE, code));
	}
}
