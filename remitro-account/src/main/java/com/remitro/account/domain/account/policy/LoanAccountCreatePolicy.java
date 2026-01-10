package com.remitro.account.domain.account.policy;

import org.springframework.stereotype.Component;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.message.ErrorMessage;

@Component
public class LoanAccountCreatePolicy {

	public void validateCreate(boolean loanApproved) {
		if (!loanApproved) {
			throw new BadRequestException(
				ErrorCode.ACCOUNT_OPERATION_NOT_ALLOWED, ErrorMessage.ACCOUNT_OPERATION_NOT_ALLOWED
			);
		}
	}
}
