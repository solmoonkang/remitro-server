package com.remitro.member.domain.member.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;

public interface MemberRepository {

	Optional<Member> findById(Long memberId);

	Optional<Member> findActiveById(Long memberId);

	Optional<Member> findActiveByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

	boolean existsByNicknameAndIdNot(String nickname, Long memberId);

	boolean existsByPhoneNumberHash(String phoneNumberHash);

	boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long memberId);

	Member save(Member member);

	Slice<Member> findDormancyCandidates(
		MemberStatus memberStatus,
		LocalDateTime lastLoginAt,
		Pageable pageable
	);

	Slice<Member> findExpiredSuspensionCandidates(
		MemberStatus memberStatus,
		LocalDateTime suspendUntil,
		Pageable pageable
	);

	Optional<Member> findWithdrawnByPhoneNumberHash(String phoneNumberHash);
}
