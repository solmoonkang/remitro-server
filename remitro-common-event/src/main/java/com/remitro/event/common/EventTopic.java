package com.remitro.event.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventTopic {

	MEMBER_EVENTS("remitro.member.events.v1");

	private final String value;
}
