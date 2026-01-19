package com.remitro.member.domain.member.repository;

import java.util.Optional;

import com.remitro.member.domain.member.model.Member;

public interface MemberRepository {

	Optional<Member> findById(Long memberId);

	Optional<Member> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

	boolean existsByNicknameAndIdNot(String nickname, Long memberId);

	boolean existsByPhoneNumber(String phoneNumber);

	boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long memberId);

	Member save(Member member);
}
