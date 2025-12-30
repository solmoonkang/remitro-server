package com.remitro.account.domain.outbox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.account.domain.outbox.model.OutboxMessage;
import com.remitro.event.domain.common.status.EventStatus;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Long> {

	List<OutboxMessage> findByEventStatusOrderByCreatedAtAsc(EventStatus eventStatus);
}
