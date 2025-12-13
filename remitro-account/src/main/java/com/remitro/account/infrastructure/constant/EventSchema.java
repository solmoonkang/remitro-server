package com.remitro.account.infrastructure.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventSchema {

	public static final int ACCOUNT_OPENED_V1 = 1;
	public static final int ACCOUNT_STATUS_UPDATED_V1 = 1;
	public static final int ACCOUNT_DEPOSITED_V1 = 1;
}
