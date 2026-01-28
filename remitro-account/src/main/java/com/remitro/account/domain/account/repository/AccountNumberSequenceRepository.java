package com.remitro.account.domain.account.repository;

public interface AccountNumberSequenceRepository {

	Long nextSequence(String type);
}
