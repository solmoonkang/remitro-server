package com.remitro.member.domain.member.repository;

import java.util.Optional;

import com.remitro.member.domain.member.model.Member;

public interface MemberRepository {

	Optional<Member> findById(Long memberId);

	Optional<Member> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

	boolean existsByPhoneNumber(String phoneNumber);

	Member save(Member member);
}
