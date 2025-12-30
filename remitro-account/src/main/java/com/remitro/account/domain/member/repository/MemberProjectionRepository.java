package com.remitro.account.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.account.domain.member.model.MemberProjection;

public interface MemberProjectionRepository extends JpaRepository<MemberProjection, Long> {
}
