package com.remitro.member.infrastructure.persistence.kyc;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.remitro.member.domain.kyc.model.KycVerification;
import com.remitro.member.domain.kyc.repository.KycVerificationQueryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaKycVerificationQueryRepository implements KycVerificationQueryRepository {

	private final SpringDataKycVerificationRepository springDataKycVerificationRepository;

	@Override
	public Optional<KycVerification> findByMemberId(Long memberId) {
		return springDataKycVerificationRepository.findByMemberId(memberId);
	}
}
