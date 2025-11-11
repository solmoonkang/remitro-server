package com.remitro.member.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.remitro.member.domain.model.OutboxMessage;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Long> {

	@Query(
		value = "SELECT * FROM MEMBER_OUTBOX_MESSAGES WHERE event_status = 'PENDING' ORDER BY outbox_message_id ASC LIMIT :limit",
		nativeQuery = true)
	List<OutboxMessage> findTopNPendingOrderByIdAsc(@Param("limit") int limit);
}
