package com.remitro.member.domain.member.model;

import org.hibernate.annotations.Comment;

import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.infrastructure.persistence.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
	name = "MEMBER",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_member_email", columnNames = "email"),
		@UniqueConstraint(name = "uk_member_nickname", columnNames = "nickname"),
		@UniqueConstraint(name = "uk_member_phone", columnNames = "phone_number"),
	}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Comment("이메일")
	@Column(name = "email", length = 100, nullable = false)
	private String email;

	@Comment("해시 비밀번호")
	@Column(name = "password", nullable = false)
	private String password;

	@Comment("닉네임")
	@Column(name = "nickname", length = 50, nullable = false)
	private String nickname;

	@Comment("전화번호")
	@Column(name = "phone_number", length = 20, nullable = false)
	private String phoneNumber;

	@Comment("회원 상태")
	@Enumerated(EnumType.STRING)
	@Column(name = "member_status", length = 20, nullable = false)
	private MemberStatus memberStatus;

	private Member(String email, String password, String nickname, String phoneNumber) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.phoneNumber = phoneNumber;
		this.memberStatus = MemberStatus.ACTIVE;
	}

	public static Member register(String email, String password, String nickname, String phoneNumber) {
		return new Member(email, password, nickname, phoneNumber);
	}

	public void updateProfile(String newNickname, String newPhoneNumber) {
		this.nickname = newNickname;
		this.phoneNumber = newPhoneNumber;
	}

	public void changePassword(String newPassword) {
		this.password = newPassword;
	}
}
