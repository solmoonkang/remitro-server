package com.remitro.member.domain.kyc.policy;

import org.springframework.stereotype.Component;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.message.ErrorMessage;
import com.remitro.member.domain.kyc.model.KycVerification;

@Component
public class KycRequestPolicy {

	public void validateRequestable(KycVerification kycVerification) {
		if (kycVerification.isVerified()) {
			throw new BadRequestException(
				ErrorCode.INVALID_REQUEST, ErrorMessage.MEMBER_KYC_ALREADY_VERIFIED
			);
		}
	}
}
