package com.remitro.member.domain.model;

import java.time.LocalDateTime;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.message.ErrorMessage;
import com.remitro.common.security.Role;
import com.remitro.member.domain.enums.ActivityStatus;
import com.remitro.member.domain.enums.KycStatus;
import com.remitro.member.domain.enums.LockReason;
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

	@Column(name = "login_failure_count", nullable = false)
	private int loginFailureCount;

	@Enumerated(EnumType.STRING)
	@Column(name = "lock_reason")
	private LockReason lockReason;

	@Column(name = "kyc_verified_at")
	private LocalDateTime kycVerifiedAt;

	@Column(name = "last_login_at")
	private LocalDateTime lastLoginAt;

	@Column(name = "locked_at")
	private LocalDateTime lockedAt;

	private static final int MAX_LOGIN_FAILURE_COUNT = 5;

	private Member(String email, String hashedPassword, String nickname, String phoneNumber) {
		this.email = email;
		this.hashedPassword = hashedPassword;
		this.nickname = nickname;
		this.phoneNumber = phoneNumber;
		this.role = Role.MEMBER;
		this.activityStatus = ActivityStatus.ACTIVE;
		this.kycStatus = KycStatus.UNVERIFIED;
		this.loginFailureCount = 0;
	}

	public static Member create(String email, String password, String nickname, String phoneNumber) {
		return new Member(email, password, nickname, phoneNumber);
	}

	public boolean isLocked() {
		return this.activityStatus == ActivityStatus.LOCKED;
	}

	public boolean isActive() {
		return this.activityStatus == ActivityStatus.ACTIVE;
	}

	public boolean isDormant() {
		return this.activityStatus == ActivityStatus.DORMANT;
	}

	/* ================= 로그인 ================= */

	public void recordLoginSuccess(LocalDateTime now) {
		this.lastLoginAt = now;
		this.loginFailureCount = 0;
	}

	public void recordLoginFailure(LocalDateTime now) {
		this.loginFailureCount++;

		if (this.loginFailureCount >= MAX_LOGIN_FAILURE_COUNT) {
			lockInternal(LockReason.LOGIN_FAILURE, now);
		}
	}

	/* ================= LOCK ================= */

	public void lockByAdmin(LocalDateTime now) {
		lockInternal(LockReason.ADMIN_ACTION, now);
	}

	public void lockBySuspiciousActivity(LocalDateTime now) {
		lockInternal(LockReason.SUSPICIOUS_ACTIVITY, now);
	}

	private void lockInternal(LockReason lockReason, LocalDateTime now) {
		if (this.activityStatus == ActivityStatus.WITHDRAWN) {
			return;
		}

		this.activityStatus = ActivityStatus.LOCKED;
		this.lockReason = lockReason;
		this.lockedAt = now;
	}

	public void unlock(LocalDateTime now) {
		validateLocked();
		resetLock(now);
	}

	private void resetLock(LocalDateTime now) {
		this.activityStatus = ActivityStatus.ACTIVE;
		this.loginFailureCount = 0;
		this.lockReason = null;
		this.lockedAt = null;
		this.lastLoginAt = now;
	}

	private void validateLocked() {
		if (!isLocked()) {
			throw new BadRequestException(
				ErrorCode.MEMBER_STATE_INVALID, ErrorMessage.MEMBER_STATE_INVALID
			);
		}
	}

	/* ================= 휴면 ================= */

	public void markDormant() {
		if (isActive()) {
			this.activityStatus = ActivityStatus.DORMANT;
		}
	}

	public void activateFromDormant(LocalDateTime now) {
		if (!isDormant()) {
			throw new BadRequestException(
				ErrorCode.MEMBER_STATE_INVALID, ErrorMessage.MEMBER_STATE_INVALID
			);
		}

		this.activityStatus = ActivityStatus.ACTIVE;
		this.lastLoginAt = now;
	}

	/* ================= KYC ================= */

	public void verifyKyc(LocalDateTime now) {
		validateKycNotFinal();
		this.kycStatus = KycStatus.VERIFIED;
		this.kycVerifiedAt = now;
	}

	public void rejectKyc() {
		validateKycNotFinal();
		this.kycStatus = KycStatus.REJECTED;
	}

	private void validateKycNotFinal() {
		if (this.kycStatus.isFinalStatus()) {
			throw new BadRequestException(
				ErrorCode.MEMBER_KYC_ALREADY_VERIFIED, ErrorMessage.MEMBER_KYC_ALREADY_VERIFIED
			);
		}
	}

	/* ================= Role ================= */

	public void changeRole(Role nextRole) {
		if (this.role == nextRole) {
			throw new BadRequestException(
				ErrorCode.ROLE_ALREADY_ASSIGNED,
				ErrorMessage.ROLE_ALREADY_ASSIGNED
			);
		}

		this.role = nextRole;
	}
}
