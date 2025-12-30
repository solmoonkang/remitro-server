package com.remitro.account.domain.member.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	@Column(name = "activity_status", nullable = false)
	private String activityStatus;

	@Column(name = "kyc_status", nullable = false)
	private String kycStatus;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@Column(name = "is_account_open_allowed", nullable = false)
	private boolean isAccountOpenAllowed;

	private MemberProjection(
		Long memberId,
		String nickname,
		String activityStatus,
		String kycStatus,
		boolean isAccountOpenAllowed
	) {
		this.memberId = memberId;
		this.nickname = nickname;
		this.activityStatus = activityStatus;
		this.kycStatus = kycStatus;
		this.updatedAt = LocalDateTime.now();
		this.isAccountOpenAllowed = isAccountOpenAllowed;
	}

	public static MemberProjection create(
		Long memberId,
		String nickname,
		String activityStatus,
		String kycStatus,
		boolean isAccountOpenAllowed
	) {
		return new MemberProjection(memberId, nickname, activityStatus, kycStatus, isAccountOpenAllowed);
	}

	public void updateActivityAndKycStatus(String activityStatus, String kycStatus, boolean isAccountOpenAllowed) {
		this.activityStatus = activityStatus;
		this.kycStatus = kycStatus;
		this.isAccountOpenAllowed = isAccountOpenAllowed;
		this.updatedAt = LocalDateTime.now();
	}
}
