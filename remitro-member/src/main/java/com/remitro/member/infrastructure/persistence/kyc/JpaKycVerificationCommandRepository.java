package com.remitro.member.infrastructure.persistence.kyc;

import org.springframework.stereotype.Repository;

import com.remitro.member.domain.kyc.model.KycVerification;
import com.remitro.member.domain.kyc.repository.KycVerificationCommandRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaKycVerificationCommandRepository implements KycVerificationCommandRepository {

	private final SpringDataKycVerificationRepository springDataKycVerificationRepository;

	@Override
	public KycVerification save(KycVerification verification) {
		return springDataKycVerificationRepository.save(verification);
	}
}
