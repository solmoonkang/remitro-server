package com.remitro.account.domain.account.policy;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.projection.model.MemberProjection;
import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.message.ErrorMessage;

@Component
public class AccountMemberPolicy {

	public void validateMember(MemberProjection memberProjection) {
		if (!memberProjection.isActive()) {
			throw new BadRequestException(
				ErrorCode.ACCOUNT_OPERATION_NOT_ALLOWED, ErrorMessage.ACCOUNT_OPERATION_NOT_ALLOWED
			);
		}
	}
}
