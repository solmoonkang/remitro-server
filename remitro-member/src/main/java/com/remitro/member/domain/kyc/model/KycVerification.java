package com.remitro.member.domain.kyc.model;

import java.time.LocalDateTime;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.message.ErrorMessage;
import com.remitro.member.domain.kyc.enums.KycStatus;

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
@Table(name = "KYC_VERIFICATIONS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KycVerification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "kyc_id")
	private Long id;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Enumerated(EnumType.STRING)
	@Column(name = "kyc_status", nullable = false)
	private KycStatus kycStatus;

	@Column(name = "reason")
	private String reason;

	@Column(name = "requested_at")
	private LocalDateTime requestedAt;

	@Column(name = "completed_at")
	private LocalDateTime completedAt;

	@Column(name = "rejected_at")
	private LocalDateTime rejectedAt;

	private KycVerification(Long memberId, LocalDateTime requestedAt) {
		this.memberId = memberId;
		this.kycStatus = KycStatus.REQUESTED;
		this.requestedAt = requestedAt;
	}

	public static KycVerification request(Long memberId, LocalDateTime requestedAt) {
		return new KycVerification(memberId, requestedAt);
	}

	public boolean isVerified() {
		return this.kycStatus == KycStatus.VERIFIED;
	}

	public boolean isRejected() {
		return this.kycStatus == KycStatus.REJECTED;
	}

	public void startVerification() {
		if (this.kycStatus != KycStatus.REQUESTED) {
			throw new BadRequestException(ErrorCode.KYC_INVALID_STATE, ErrorMessage.KYC_CANNOT_START);
		}
		this.kycStatus = KycStatus.IN_PROGRESS;
	}

	public void verify(LocalDateTime completedAt) {
		if (this.kycStatus != KycStatus.IN_PROGRESS) {
			throw new BadRequestException(ErrorCode.KYC_INVALID_STATE, ErrorMessage.KYC_CANNOT_VERIFY);
		}
		this.kycStatus = KycStatus.VERIFIED;
		this.completedAt = completedAt;
	}

	public void reject(String reason, LocalDateTime rejectedAt) {
		if (this.kycStatus != KycStatus.IN_PROGRESS) {
			throw new BadRequestException(ErrorCode.KYC_INVALID_STATE, ErrorMessage.KYC_CANNOT_REJECT);
		}
		this.kycStatus = KycStatus.REJECTED;
		this.reason = reason;
		this.rejectedAt = rejectedAt;
	}
}
