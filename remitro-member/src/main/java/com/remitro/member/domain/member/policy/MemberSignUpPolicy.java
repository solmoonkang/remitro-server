package com.remitro.member.domain.member.policy;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.ConflictException;
import com.remitro.common.error.message.ErrorMessage;

public class MemberSignUpPolicy {

	public void validateDuplicated(boolean emailExists, boolean nicknameExists, boolean phoneExists) {
		if (emailExists) {
			throw new ConflictException(
				ErrorCode.EMAIL_ALREADY_EXISTS, ErrorMessage.EMAIL_ALREADY_EXISTS
			);
		}

		if (nicknameExists) {
			throw new ConflictException(
				ErrorCode.NICKNAME_ALREADY_EXISTS, ErrorMessage.NICKNAME_ALREADY_EXISTS
			);
		}

		if (phoneExists) {
			throw new ConflictException(
				ErrorCode.PHONE_NUMBER_ALREADY_EXISTS, ErrorMessage.PHONE_NUMBER_ALREADY_EXISTS
			);
		}
	}
}
