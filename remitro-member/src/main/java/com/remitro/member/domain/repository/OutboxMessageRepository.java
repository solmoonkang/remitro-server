package com.remitro.member.domain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.common.domain.enums.EventStatus;
import com.remitro.member.domain.model.OutboxMessage;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Long> {

	List<OutboxMessage> findByEventStatusOrderByIdAsc(EventStatus eventStatus, Pageable pageable);
}
