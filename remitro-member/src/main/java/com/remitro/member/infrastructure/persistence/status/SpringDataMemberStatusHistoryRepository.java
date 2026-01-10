package com.remitro.member.infrastructure.persistence.status;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.member.domain.status.model.MemberStatusHistory;

public interface SpringDataMemberStatusHistoryRepository extends JpaRepository<MemberStatusHistory, Long> {
}
