package com.remitro.member.domain.member.model;

import java.time.LocalDateTime;

import com.remitro.common.security.Role;
import com.remitro.member.infrastructure.persistence.base.BaseTimeEntity;

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

	@Column(name = "login_failure_count", nullable = false)
	private int loginFailureCount;

	@Enumerated(EnumType.STRING)
	@Column(name = "lock_reason")
	private LockReason lockReason;

	@Column(name = "last_login_at")
	private LocalDateTime lastLoginAt;

	@Column(name = "locked_at")
	private LocalDateTime lockedAt;

	private Member(String email, String hashedPassword, String nickname, String phoneNumber) {
		this.email = email;
		this.hashedPassword = hashedPassword;
		this.nickname = nickname;
		this.phoneNumber = phoneNumber;
		this.role = Role.MEMBER;
		this.activityStatus = ActivityStatus.ACTIVE;
		this.loginFailureCount = 0;
	}

	public static Member signUp(
		String email,
		String hashedPassword,
		String nickname,
		String phoneNumber
	) {
		return new Member(email, hashedPassword, nickname, phoneNumber);
	}

	public void changePassword(String encodedPassword) {
		this.hashedPassword = encodedPassword;
	}

	public void updateProfile(String nickname, String phoneNumber) {
		this.nickname = nickname;
		this.phoneNumber = phoneNumber;
	}

	public boolean isWithdrawn() {
		return this.activityStatus == ActivityStatus.WITHDRAWN;
	}

	public boolean isDormant() {
		return this.activityStatus == ActivityStatus.DORMANT;
	}
}
