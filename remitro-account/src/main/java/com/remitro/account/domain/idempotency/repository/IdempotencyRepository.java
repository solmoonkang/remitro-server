package com.remitro.account.domain.idempotency.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.account.domain.idempotency.model.Idempotency;

public interface IdempotencyRepository extends JpaRepository<Idempotency, String> {
}
