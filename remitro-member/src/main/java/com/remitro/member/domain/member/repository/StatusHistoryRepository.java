package com.remitro.member.domain.member.repository;

import java.util.Collection;

import com.remitro.member.domain.member.model.StatusHistory;

public interface StatusHistoryRepository {

	void save(StatusHistory statusHistory);

	void saveAll(Collection<StatusHistory> statusHistories);
}
