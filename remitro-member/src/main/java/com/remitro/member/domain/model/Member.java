package com.remitro.member.domain.model;

import java.time.LocalDateTime;

import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.model.ErrorMessage;
import com.remitro.common.security.Role;
import com.remitro.member.domain.enums.ActivityStatus;
import com.remitro.member.domain.enums.KycStatus;
import com.remitro.member.infrastructure.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "MEMBERS", indexes = {
	@Index(name = "idx_members_activity_status", columnList = "activity_status")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id", unique = true, nullable = false)
	private Long id;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "hashed_password", nullable = false)
	private String hashedPassword;

	@Column(name = "nickname", unique = true, nullable = false)
	private String nickname;

	@Column(name = "phone_number", unique = true, nullable = false)
	private String phoneNumber;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role role;

	@Enumerated(EnumType.STRING)
	@Column(name = "activity_status", nullable = false)
	private ActivityStatus activityStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "kyc_status", nullable = false)
	private KycStatus kycStatus;

	@Column(name = "kyc_verified_at")
	private LocalDateTime kycVerifiedAt;

	private Member(String email, String hashedPassword, String nickname, String phoneNumber) {
		this.email = email;
		this.hashedPassword = hashedPassword;
		this.nickname = nickname;
		this.phoneNumber = phoneNumber;
		this.role = Role.MEMBER;
		this.activityStatus = ActivityStatus.ACTIVE;
		this.kycStatus = KycStatus.UNVERIFIED;
	}

	public static Member create(String email, String password, String nickname, String phoneNumber) {
		return new Member(email, password, nickname, phoneNumber);
	}

	public void updateActivityStatus(ActivityStatus nextActivityStatus) {
		if (this.activityStatus.isTerminal()) {
			throw new BadRequestException(ErrorMessage.MEMBER_ALREADY_WITHDRAWN);
		}

		if (this.activityStatus == ActivityStatus.LOCKED && nextActivityStatus == ActivityStatus.ACTIVE) {
			throw new BadRequestException(ErrorMessage.INVALID_ACTIVITY_STATUS_TRANSITION);
		}

		this.activityStatus = nextActivityStatus;
	}

	public void updateKycStatus(KycStatus nextKycStatus) {
		if (this.kycStatus.isFinalStatus()) {
			throw new BadRequestException(ErrorMessage.KYC_ALREADY_VERIFIED);
		}

		if (nextKycStatus == KycStatus.VERIFIED) {
			this.kycVerifiedAt = LocalDateTime.now();
		}

		this.kycStatus = nextKycStatus;
	}

	public boolean isActiveForAccountOpen() {
		return this.activityStatus == ActivityStatus.ACTIVE
			&& this.kycStatus == KycStatus.VERIFIED;
	}

	public void updateRole(Role nextRole) {
		if (this.role == nextRole) {
			throw new BadRequestException(ErrorMessage.ROLE_ALREADY_ASSIGNED);
		}

		this.role = nextRole;
	}
}
