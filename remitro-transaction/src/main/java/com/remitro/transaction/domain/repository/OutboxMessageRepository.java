package com.remitro.transaction.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.transaction.domain.model.OutboxMessage;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Long> {
}
