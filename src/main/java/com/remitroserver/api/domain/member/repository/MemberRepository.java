package com.remitroserver.api.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitroserver.api.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findMemberByEmail(String email);

	boolean existsMemberByEmail(String email);

	boolean existsMemberByNickname(String nickname);

	boolean existsMemberByRegistrationNumber(String registrationNumber);
}
