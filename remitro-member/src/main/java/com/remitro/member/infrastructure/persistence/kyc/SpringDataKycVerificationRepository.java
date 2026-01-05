package com.remitro.member.infrastructure.persistence.kyc;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.member.domain.kyc.model.KycVerification;

public interface SpringDataKycVerificationRepository extends JpaRepository<KycVerification, Long> {

}
