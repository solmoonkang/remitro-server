package com.remitro.member.infrastructure.persistence.kyc;

import org.springframework.stereotype.Repository;

import com.remitro.member.domain.kyc.repository.KycVerificationQueryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaKycVerificationQueryRepository implements KycVerificationQueryRepository {

	private final SpringDataKycVerificationRepository springDataKycVerificationRepository;
}
