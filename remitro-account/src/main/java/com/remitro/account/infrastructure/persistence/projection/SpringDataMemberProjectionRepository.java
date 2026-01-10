package com.remitro.account.infrastructure.persistence.projection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.account.domain.projection.model.MemberProjection;

public interface SpringDataMemberProjectionRepository extends JpaRepository<MemberProjection, Long> {
}
