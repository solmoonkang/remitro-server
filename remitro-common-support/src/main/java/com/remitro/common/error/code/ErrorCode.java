package com.remitro.common.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	/* ==========================
	 * AUTH
	 * ========================== */
	PASSWORD_INVALID("AUTH_400_001"),

	TOKEN_INVALID("AUTH_401_001"),
	TOKEN_EXPIRED("AUTH_401_002"),
	AUTHENTICATION_NOT_ALLOWED("AUTH_401_003"),

	/* ==========================
	 * MEMBER / KYC
	 * ========================== */
	KYC_INVALID_STATE("KYC_400_001"),

	MEMBER_STATE_INVALID("MEMBER_400_001"),

	MEMBER_LOCKED("MEMBER_401_001"),
	MEMBER_WITHDRAWN("MEMBER_401_002"),
	MEMBER_DORMANT("MEMBER_401_003"),

	MEMBER_NOT_FOUND("MEMBER_404_001"),

	MEMBER_DUPLICATED_RESOURCE("MEMBER_409_001"),

	/* ==========================
	 * ACCOUNT
	 * ========================== */
	ACCOUNT_OPERATION_NOT_ALLOWED("ACCOUNT_400_001"),

	ACCOUNT_NOT_FOUND("ACCOUNT_404_001"),

	ACCOUNT_LIMIT_EXCEEDED("ACCOUNT_409_001"),
	ACCOUNT_NUMBER_DUPLICATED("ACCOUNT_409_002"),

	/* ==========================
	 * TRANSACTION
	 * ========================== */
	TRANSACTION_NOT_FOUND("TRANSACTION_404_001"),

	INSUFFICIENT_FUNDS("TRANSACTION_409_001"),

	/* ==========================
	 * COMMON
	 * ========================== */
	INVALID_REQUEST("COMMON_400_001"),

	ACCESS_FORBIDDEN("COMMON_403_001"),

	DUPLICATE_REQUEST("COMMON_409_001"),

	INTERNAL_SERVER_ERROR("COMMON_500_001"),

	TEMPORARY_UNAVAILABLE("COMMON_503_001");

	private final String code;
}
