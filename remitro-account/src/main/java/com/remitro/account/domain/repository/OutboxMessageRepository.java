package com.remitro.account.domain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.account.domain.event.EventStatus;
import com.remitro.account.domain.model.OutboxMessage;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Long> {

	List<OutboxMessage> findByEventStatusOrderByCreatedAtAsc(EventStatus eventStatus, Pageable pageable);
}
