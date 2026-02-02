package com.remitro.account.domain.projection.repository;

import java.util.Optional;

import com.remitro.account.domain.projection.model.MemberProjection;

public interface MemberProjectionRepository {

	Optional<MemberProjection> findByMemberId(Long memberId);

	MemberProjection save(MemberProjection memberProjection);
}
