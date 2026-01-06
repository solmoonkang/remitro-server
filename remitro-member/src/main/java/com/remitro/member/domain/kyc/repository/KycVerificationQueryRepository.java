package com.remitro.member.domain.kyc.repository;

import java.util.Optional;

import com.remitro.member.domain.kyc.model.KycVerification;

public interface KycVerificationQueryRepository {

	Optional<KycVerification> findByMemberId(Long memberId);
}
