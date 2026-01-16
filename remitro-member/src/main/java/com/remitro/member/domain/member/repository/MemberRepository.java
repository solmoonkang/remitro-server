package com.remitro.member.domain.member.repository;

import com.remitro.member.domain.member.model.Member;

public interface MemberRepository {

	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

	boolean existsByPhoneNumber(String phoneNumber);

	Member save(Member member);
}
