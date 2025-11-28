package com.remitro.member.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.member.domain.model.KycVerification;

public interface KycVerificationRepository extends JpaRepository<KycVerification, Long> {

	Optional<KycVerification> findTopByMemberIdOrderByRequestedAtDesc(Long memberId);
}
