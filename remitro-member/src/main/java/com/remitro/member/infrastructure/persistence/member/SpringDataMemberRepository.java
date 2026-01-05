package com.remitro.member.infrastructure.persistence.member;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.member.domain.member.model.Member;

public interface SpringDataMemberRepository extends JpaRepository<Member, Long> {

	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

	boolean existsByPhoneNumber(String phoneNumber);
}
