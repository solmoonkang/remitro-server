package com.remitro.account.infrastructure.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberEventType {

	public static final String MEMBER_CREATED_EVENT = "MEMBER_CREATED";
	public static final String MEMBER_STATUS_UPDATED_EVENT = "MEMBER_STATUS_UPDATED";
	public static final String MEMBER_KYC_UPDATED_EVENT = "MEMBER_KYC_UPDATED";
}
