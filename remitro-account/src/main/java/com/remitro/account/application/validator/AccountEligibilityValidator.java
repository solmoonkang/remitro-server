package com.remitro.account.application.validator;

import java.util.List;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.enums.AccountType;
import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.MemberProjection;
import com.remitro.common.error.exception.ConflictException;
import com.remitro.common.error.model.ErrorMessage;

@Component
public class AccountEligibilityValidator {

	public static final int LIMIT_PER_TYPE = 3;
	public static final int MAX_ACCOUNT_PER_MEMBER = 10;

	public void validateMemberIsActive(MemberProjection memberProjection) {
		if (!memberProjection.isAccountOpenAllowed()) {
			throw new ConflictException(ErrorMessage.MEMBER_NOT_ELIGIBLE);
		}
	}

	public void validateAccountTypeLimit(List<Account> existingAccounts, AccountType targetAccountType) {
		long count = existingAccounts.stream()
			.filter(account -> account.getAccountType() == targetAccountType)
			.count();

		if (count >= LIMIT_PER_TYPE) {
			throw new ConflictException(ErrorMessage.ACCOUNT_TYPE_LIMIT_EXCEEDED);
		}
	}

	public void validateOverallAccountLimit(long totalAccounts) {
		if (totalAccounts >= MAX_ACCOUNT_PER_MEMBER) {
			throw new ConflictException(ErrorMessage.ACCOUNT_LIMIT_EXCEEDED);
		}
	}
}
