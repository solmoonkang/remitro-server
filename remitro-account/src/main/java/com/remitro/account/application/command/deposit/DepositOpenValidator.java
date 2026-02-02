package com.remitro.account.application.command.deposit;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.enums.AccountType;
import com.remitro.account.domain.account.policy.LimitPolicy;
import com.remitro.account.domain.account.policy.OpenPolicy;
import com.remitro.account.domain.projection.model.MemberProjection;
import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.BadRequestException;
import com.remitro.support.exception.ForbiddenException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DepositOpenValidator {

	private final OpenPolicy openPolicy;
	private final LimitPolicy limitPolicy;

	public void validateMemberOpenable(MemberProjection member) {
		if (openPolicy.isRestricted(member)) {
			throw new ForbiddenException(ErrorCode.MEMBER_INACTIVE);
		}
	}

	public void validateAccountTypeOpenable(AccountType accountType) {
		if (openPolicy.isNotOpenable(accountType)) {
			throw new BadRequestException(ErrorCode.ACCOUNT_TYPE_NOT_OPENABLE, accountType.getName());
		}
	}

	public void validateAccountOpenLimit(Long memberId, AccountType accountType) {
		if (limitPolicy.isExceeded(memberId, accountType)) {
			throw new BadRequestException(ErrorCode.ACCOUNT_LIMIT_EXCEEDED);
		}
	}

	public String trimAccountAlias(String accountAlias) {
		if (accountAlias == null) {
			return null;
		}

		return accountAlias.trim();
	}
}
