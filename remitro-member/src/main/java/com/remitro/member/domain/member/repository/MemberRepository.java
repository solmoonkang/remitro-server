package com.remitro.member.domain.member.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.remitro.member.domain.member.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

	boolean existsByPhoneNumber(String phoneNumber);

	Optional<Member> findByEmail(String email);

	@Query("SELECT m FROM Member m "
		+ "WHERE m.activityStatus = 'ACTIVE' "
		+ "AND m.lastLoginAt IS NOT NULL "
		+ "AND m.lastLoginAt < :threshold"
	)
	List<Member> findActiveMembersWithLastLoginBefore(@Param("threshold") LocalDateTime threshold);
}
