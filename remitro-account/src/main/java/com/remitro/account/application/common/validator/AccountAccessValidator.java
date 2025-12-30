package com.remitro.account.application.common.validator;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.model.Account;
import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.ForbiddenException;
import com.remitro.common.error.message.ErrorMessage;

@Component
public class AccountAccessValidator {

	public void validateOwner(Account account, Long memberId) {
		if (!account.getMemberId().equals(memberId)) {
			throw new ForbiddenException(
				ErrorCode.ACCOUNT_ACCESS_FORBIDDEN, ErrorMessage.ACCOUNT_ACCESS_FORBIDDEN
			);
		}
	}
}
