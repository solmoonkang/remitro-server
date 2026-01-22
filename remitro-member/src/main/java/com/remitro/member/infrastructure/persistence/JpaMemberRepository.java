package com.remitro.member.infrastructure.persistence;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.repository.MemberRepository;

public interface JpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

	@Query("SELECT m "
		+ "FROM Member m "
		+ "WHERE m.memberStatus = :memberStatus AND m.lastLoginAt < :lastLoginAt"
	)
	Slice<Member> findDormancyCandidates(
		@Param("memberStatus") MemberStatus memberStatus,
		@Param("lastLoginAt") LocalDateTime lastLoginAt,
		Pageable pageable
	);

	@Query("SELECT m "
		+ "FROM Member m "
		+ "WHERE m.memberStatus = :memberStatus AND m.suspendUntil IS NOT NULL AND m.suspendUntil < :suspendUntil"
	)
	Slice<Member> findExpiredSuspensionCandidates(
		@Param("memberStatus") MemberStatus memberStatus,
		@Param("suspendUntil") LocalDateTime suspendUntil,
		Pageable pageable
	);
}
