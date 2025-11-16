package com.remitro.account.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.account.domain.model.MemberProjection;

public interface MemberProjectionRepository extends JpaRepository<MemberProjection, Long> {
}
