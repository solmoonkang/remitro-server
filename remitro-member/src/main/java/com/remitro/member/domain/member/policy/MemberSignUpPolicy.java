package com.remitro.member.domain.member.policy;

import org.springframework.stereotype.Component;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.ConflictException;
import com.remitro.common.error.message.ErrorMessage;

@Component
public class MemberSignUpPolicy {

	public void validateDuplicated(boolean emailExists, boolean nicknameExists, boolean phoneExists) {
		if (emailExists) {
			throw new ConflictException(
				ErrorCode.MEMBER_DUPLICATED_RESOURCE, ErrorMessage.DUPLICATE_EMAIL
			);
		}

		if (nicknameExists) {
			throw new ConflictException(
				ErrorCode.MEMBER_DUPLICATED_RESOURCE, ErrorMessage.DUPLICATE_NICKNAME
			);
		}

		if (phoneExists) {
			throw new ConflictException(
				ErrorCode.MEMBER_DUPLICATED_RESOURCE, ErrorMessage.DUPLICATE_PHONE_NUMBER
			);
		}
	}
}
