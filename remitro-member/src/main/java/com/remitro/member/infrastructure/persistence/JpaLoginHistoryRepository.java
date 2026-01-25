package com.remitro.member.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.member.domain.audit.model.LoginHistory;
import com.remitro.member.domain.audit.repository.LoginHistoryRepository;

public interface JpaLoginHistoryRepository extends JpaRepository<LoginHistory, Long>, LoginHistoryRepository {
}
