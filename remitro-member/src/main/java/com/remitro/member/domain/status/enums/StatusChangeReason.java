package com.remitro.member.domain.status.enums;

public enum StatusChangeReason {

	ADMIN_UNLOCK,
	ADMIN_WITHDRAW,

	LOGIN_FAILURE_LOCK,
	LOGIN_SUCCESS_UNLOCK,

	DORMANT_BY_BATCH,
	DORMANT_ACTIVATED_BY_LOGIN
}
