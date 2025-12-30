package com.remitro.member.domain.outbox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.event.domain.common.status.EventStatus;
import com.remitro.member.domain.outbox.model.OutboxMessage;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Long> {

	List<OutboxMessage> findByEventStatusOrderByIdAsc(EventStatus eventStatus);
}
