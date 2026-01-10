package com.remitro.member.infrastructure.persistence.member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.remitro.member.domain.member.enums.ActivityStatus;
import com.remitro.member.domain.member.model.Member;

public interface SpringDataMemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

	boolean existsByPhoneNumber(String phoneNumber);

	@Query("SELECT m FROM Member m WHERE m.activityStatus = :activityStatus AND m.lastLoginAt < :threshold")
	List<Member> findActiveMembersInactiveSince(ActivityStatus activityStatus, LocalDateTime threshold);
}
