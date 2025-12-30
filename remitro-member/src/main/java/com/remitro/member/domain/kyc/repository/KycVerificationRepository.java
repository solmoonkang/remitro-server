package com.remitro.member.domain.kyc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.member.domain.kyc.enums.KycVerificationStatus;
import com.remitro.member.domain.kyc.model.KycVerification;

public interface KycVerificationRepository extends JpaRepository<KycVerification, Long> {

	Optional<KycVerification> findTopByMemberIdOrderByRequestedAtDesc(Long memberId);

	List<KycVerification> findAllByKycVerificationStatusOrderByRequestedAtAsc(
		KycVerificationStatus kycVerificationStatus
	);
}
