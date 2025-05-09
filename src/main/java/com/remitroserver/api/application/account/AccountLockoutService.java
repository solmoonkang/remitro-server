package com.remitroserver.api.application.account;

import static com.remitroserver.global.common.util.AccountConstant.*;
import static com.remitroserver.global.error.model.ErrorMessage.*;

import org.springframework.stereotype.Service;

import com.remitroserver.api.domain.account.repository.AccountRedisRepository;
import com.remitroserver.global.error.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountLockoutService {

	private final AccountRedisRepository accountRedisRepository;

	public void validateAccountNotLocked(Long accountId) {
		if (accountRedisRepository.isAccountLocked(accountId)) {
			final long remainingMinutes = accountRedisRepository.getLockRemainingMinutes(accountId);
			throw new BadRequestException(String.format(ACCOUNT_PASSWORD_LOCKED_ERROR.getMessage(), remainingMinutes));
		}
	}

	public void validateEnforcePasswordLockout(Long accountId) {
		final long failedCount = accountRedisRepository.incrementAccountFailedAttempts(accountId);

		if (failedCount >= MAX_FAILED) {
			accountRedisRepository.lockAccount(accountId);
			accountRedisRepository.resetFailedAttempts(accountId);
			throw new BadRequestException(
				String.format(ACCOUNT_PASSWORD_LOCKED_AFTER_MAX_ERROR.getMessage(), MAX_FAILED, LOCK_MINUTES));
		}

		final long remainingTime = MAX_FAILED - failedCount;
		throw new BadRequestException(String.format(ACCOUNT_PASSWORD_MISMATCH_ERROR.getMessage(), remainingTime));
	}

	public void resetFailedAttempts(Long accountId) {
		accountRedisRepository.resetFailedAttempts(accountId);
	}
}
