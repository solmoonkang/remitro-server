package com.remitro.member.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.member.domain.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

	Optional<Member> findByEmail(String email);
}
