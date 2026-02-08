package com.remitro.member.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.remitro.member.domain.outbox.model.OutboxEvent;
import com.remitro.member.domain.outbox.repository.OutboxEventRepository;

public interface JpaOutboxEventRepository extends JpaRepository<OutboxEvent, Long>, OutboxEventRepository {

	@Query(value = """
		    SELECT * FROM OUTBOX_EVENTS 
		    WHERE outbox_status = 'PENDING' 
		    AND retry_count < 5
		    ORDER BY created_at ASC 
		    LIMIT :limit 
		    FOR UPDATE SKIP LOCKED
		""", nativeQuery = true)
	List<OutboxEvent> findAllPendingEventsForUpdate(@Param("limit") int limit);
}
