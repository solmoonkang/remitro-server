package com.remitro.member.infrastructure.persistence;

import org.springframework.data.repository.CrudRepository;

import com.remitro.member.domain.verification.model.Verification;
import com.remitro.member.domain.verification.repository.VerificationRepository;

public interface RedisVerificationRepository extends CrudRepository<Verification, String>, VerificationRepository {
}
