package com.remitroserver.api.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitroserver.api.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
