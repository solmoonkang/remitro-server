package com.remitro.account.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.account.domain.projection.model.MemberProjection;
import com.remitro.account.domain.projection.repository.MemberProjectionRepository;

public interface JpaMemberProjectionRepository extends
	JpaRepository<MemberProjection, Long>, MemberProjectionRepository {
}
