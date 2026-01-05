package com.remitro.member.domain.member.repository;

import java.util.Optional;

import com.remitro.member.domain.member.model.Member;

public interface MemberQueryRepository {

	Optional<Member> findById(Long id);

	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

	boolean existsByPhoneNumber(String phoneNumber);
}
