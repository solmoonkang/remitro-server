package com.remitro.account.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remitro.account.domain.model.Idempotency;

public interface IdempotencyRepository extends JpaRepository<Idempotency, String> {
}
