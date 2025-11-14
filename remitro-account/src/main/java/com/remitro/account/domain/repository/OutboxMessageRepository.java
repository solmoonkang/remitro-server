package com.remitro.account.domain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.remitro.account.domain.model.OutboxMessage;
import com.remitro.common.domain.enums.EventStatus;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Long> {

	List<OutboxMessage> findByEventStatusOrderByCreatedAtAsc(EventStatus eventStatus, Pageable pageable);
}
