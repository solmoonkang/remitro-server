package com.remitro.account.domain.account.policy;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.message.ErrorMessage;

@Component
public class VirtualAccountIssuePolicy {

	public void validateIssue(LocalDateTime expiredAt) {
		if (expiredAt.isBefore(LocalDateTime.now())) {
			throw new BadRequestException(
				ErrorCode.ACCOUNT_OPERATION_NOT_ALLOWED, ErrorMessage.ACCOUNT_OPERATION_NOT_ALLOWED
			);
		}
	}
}
