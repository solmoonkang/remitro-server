package com.remitro.member.domain.kyc.repository;

import com.remitro.member.domain.kyc.model.KycVerification;

public interface KycVerificationCommandRepository {

	KycVerification save(KycVerification kycVerification);
}
