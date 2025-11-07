package com.remitro.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaConstant {

	public static final String TOPIC_TRANSACTION_EVENTS = "remitro.events.transactions";
	public static final String GROUP_TRANSACTION_CONSUMERS = "remitro.transaction.group";
}
