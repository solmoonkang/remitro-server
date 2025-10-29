package com.remitro.account.infrastructure.outbox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.remitro.account.infrastructure.outbox.model.OutboxMessage;
import com.remitro.common.common.entity.enums.EventStatus;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Long> {

	@Query("SELECT om FROM OutboxMessage om WHERE om.eventStatus = :eventStatus ORDER BY om.createdAt ASC LIMIT :limit")
	List<OutboxMessage> findPendingEvents(@Param("eventStatus") EventStatus eventStatus, @Param("limit") int limit);
}
