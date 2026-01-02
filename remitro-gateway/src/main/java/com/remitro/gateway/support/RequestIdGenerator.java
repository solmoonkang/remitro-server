package com.remitro.gateway.support;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestIdGenerator {

	public static String generate() {
		return UUID.randomUUID().toString();
	}
}
