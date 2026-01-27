package com.remitro.member.domain.audit.repository;

import com.remitro.member.domain.audit.model.LoginHistory;

public interface LoginHistoryRepository {

	LoginHistory save(LoginHistory loginHistory);
}
