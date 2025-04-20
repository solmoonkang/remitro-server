package com.remitroserver.api.domain.member.entity;

import com.remitroserver.global.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TBL_MEMBERS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "password", unique = true, nullable = false)
	private String password;

	@Column(name = "registration_number", unique = true, nullable = false)
	private String registrationNumber;

	@Column(name = "nickname", unique = true, nullable = false)
	private String nickname;

	private Member(String email, String password, String registrationNumber, String nickname) {
		this.email = email;
		this.password = password;
		this.registrationNumber = registrationNumber;
		this.nickname = nickname;
	}
}
