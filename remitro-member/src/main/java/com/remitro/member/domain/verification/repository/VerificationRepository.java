package com.remitro.member.domain.verification.repository;

import java.util.Optional;

import com.remitro.member.domain.verification.model.Verification;

public interface VerificationRepository {

	Optional<Verification> findById(String email);

	Optional<Verification> findByIdAndVerificationToken(String email, String verificationToken);

	Verification save(Verification verification);

	void delete(Verification verification);
}
