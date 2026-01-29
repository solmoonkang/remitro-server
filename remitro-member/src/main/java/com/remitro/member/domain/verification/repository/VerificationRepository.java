package com.remitro.member.domain.verification.repository;

import java.util.Optional;

import com.remitro.member.domain.verification.model.Verification;

public interface VerificationRepository {

	Optional<Verification> findByEmail(String email);

	void save(Verification verification);

	void deleteByEmail(String email);
}
