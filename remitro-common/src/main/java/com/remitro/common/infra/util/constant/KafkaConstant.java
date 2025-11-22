package com.remitro.common.infra.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaConstant {

	public static final String EVENT_HEADER_ID = "eventId";
	public static final String EVENT_HEADER_TYPE = "eventType";

	// ACCOUNT
	public static final String MEMBER_EVENTS_TOPIC_NAME = "${topics.member-events}";
	public static final String ACCOUNT_CONSUMER_GROUP_ID = "account-service";

	// TRANSACTION
	public static final String ACCOUNT_EVENTS_TOPIC_NAME = "${topics.account-events}";
	public static final String TRANSACTION_CONSUMER_GROUP_ID = "transaction-service";
}
