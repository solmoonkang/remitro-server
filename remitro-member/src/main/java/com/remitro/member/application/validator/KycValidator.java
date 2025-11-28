package com.remitro.member.application.validator;

import org.springframework.stereotype.Component;

import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.model.ErrorMessage;
import com.remitro.member.application.dto.request.UpdateKycStatusRequest;
import com.remitro.member.domain.enums.KycStatus;

@Component
public class KycValidator {

	public void validateCompletionRequest(UpdateKycStatusRequest updateKycStatusRequest) {
		if (updateKycStatusRequest.kycStatus() != KycStatus.VERIFIED) {
			return;
		}

		if (updateKycStatusRequest.reason() == null || updateKycStatusRequest.reason().isBlank()) {
			throw new BadRequestException(ErrorMessage.INVALID_KYC_STATUS_REASON);
		}
	}
}
