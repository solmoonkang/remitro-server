package com.remitro.member.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.member.domain.audit.model.StatusHistory;
import com.remitro.member.domain.audit.repository.StatusHistoryRepository;

public interface JpaStatusHistoryRepository extends
	JpaRepository<StatusHistory, Long>, StatusHistoryRepository {
}
