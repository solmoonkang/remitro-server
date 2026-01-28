package com.remitro.account.application.command.deposit;

import org.springframework.stereotype.Component;

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

	public void validateMember(MemberProjection member) {
		if (openPolicy.isRestricted(member)) {
			throw new ForbiddenException(ErrorCode.MEMBER_INACTIVE);
		}
	}

	public void validateLimit(Long memberId) {
		if (limitPolicy.isExceeded(memberId)) {
			throw new BadRequestException(ErrorCode.ACCOUNT_LIMIT_EXCEEDED);
		}
	}
}
