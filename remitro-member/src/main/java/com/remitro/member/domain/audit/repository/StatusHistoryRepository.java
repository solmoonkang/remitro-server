package com.remitro.member.domain.audit.repository;

import java.util.List;

import com.remitro.member.domain.audit.model.StatusHistory;

public interface StatusHistoryRepository {

	StatusHistory save(StatusHistory statusHistory);

	<S extends StatusHistory> List<S> saveAll(Iterable<S> statusHistories);
}
