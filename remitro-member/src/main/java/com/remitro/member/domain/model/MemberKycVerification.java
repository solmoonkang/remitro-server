package com.remitro.member.domain.model;

import java.time.LocalDateTime;

import com.remitro.member.domain.model.enums.KycVerificationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "MEMBER_KYC_VERIFICATIONS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberKycVerification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "kyc_id")
	private Long id;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Enumerated(EnumType.STRING)
	@Column(name = "kyc_verification_status", nullable = false)
	private KycVerificationStatus kycVerificationStatus;

	@Column(name = "requested_at", nullable = false)
	private LocalDateTime requestedAt;

	@Column(name = "completed_at")
	private LocalDateTime completedAt;

	@Column(name = "reason")
	private String reason;

	private MemberKycVerification(Long memberId) {
		this.memberId = memberId;
		this.kycVerificationStatus = KycVerificationStatus.PENDING;
		this.requestedAt = LocalDateTime.now();
	}
}
