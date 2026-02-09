package com.remitro.member.domain.outbox.repository;

import java.util.List;

import com.remitro.member.domain.outbox.model.OutboxEvent;

public interface OutboxEventRepository {

	OutboxEvent save(OutboxEvent outboxEvent);

	List<OutboxEvent> findAllPendingEventsForUpdate(int limit);
}
