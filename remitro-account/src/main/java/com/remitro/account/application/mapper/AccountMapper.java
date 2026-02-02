package com.remitro.account.application.mapper;

import java.time.LocalDateTime;

import com.remitro.account.application.command.dto.response.DepositOpenResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountMapper {

	public static DepositOpenResponse toDepositOpenResponse(
		String accountNumber,
		String accountAlias,
		LocalDateTime createdAt
	) {
		return new DepositOpenResponse(
			accountNumber,
			accountAlias,
			createdAt
		);
	}
}
