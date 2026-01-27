package com.remitro.member.domain.verification.repository;

import java.util.Optional;

import com.remitro.member.domain.verification.model.Verification;

public interface VerificationRepository {

	Optional<Verification> findByEmail(String email);

	Verification save(Verification verification);

	void delete(Verification verification);
}
