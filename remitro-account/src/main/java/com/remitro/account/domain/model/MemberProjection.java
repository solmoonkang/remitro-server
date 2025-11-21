package com.remitro.account.domain.model;

import java.time.LocalDateTime;

import com.remitro.common.contract.member.ActivityStatus;
import com.remitro.common.contract.member.KycStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "MEMBER_PROJECTION", indexes = {
	@Index(name = "idx_member_id", columnList = "member_id")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProjection {

	@Id
	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Column(name = "nickname", nullable = false, length = 50)
	private String nickname;

	@Enumerated(EnumType.STRING)
	@Column(name = "activity_status", nullable = false)
	private ActivityStatus activityStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "kyc_status", nullable = false)
	private KycStatus kycStatus;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	private MemberProjection(Long memberId, String nickname, ActivityStatus activityStatus, LocalDateTime updatedAt) {
		this.memberId = memberId;
		this.nickname = nickname;
		this.activityStatus = activityStatus;
		this.updatedAt = updatedAt;
		this.kycStatus = KycStatus.UNVERIFIED;
	}

	public static MemberProjection create(Long memberId, String nickname, ActivityStatus activityStatus) {
		return new MemberProjection(memberId, nickname, activityStatus, LocalDateTime.now());
	}

	public void update(ActivityStatus activityStatus) {
		this.activityStatus = activityStatus;
		this.updatedAt = LocalDateTime.now();
	}
}
