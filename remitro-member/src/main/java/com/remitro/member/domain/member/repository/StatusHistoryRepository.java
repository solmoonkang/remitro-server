package com.remitro.member.domain.member.repository;

import java.util.List;

import com.remitro.member.domain.member.model.StatusHistory;

public interface StatusHistoryRepository {

	StatusHistory save(StatusHistory statusHistory);

	<S extends StatusHistory> List<S> saveAll(Iterable<S> statusHistories);
}
