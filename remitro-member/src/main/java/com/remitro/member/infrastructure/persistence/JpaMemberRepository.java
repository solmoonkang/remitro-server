package com.remitro.member.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.repository.MemberRepository;

public interface JpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {
}
