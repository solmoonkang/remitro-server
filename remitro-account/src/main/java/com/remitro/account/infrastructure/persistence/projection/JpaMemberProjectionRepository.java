package com.remitro.account.infrastructure.persistence.projection;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.remitro.account.domain.projection.model.MemberProjection;
import com.remitro.account.domain.projection.repository.MemberProjectionRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaMemberProjectionRepository implements MemberProjectionRepository {

	private final SpringDataMemberProjectionRepository springDataMemberProjectionRepository;

	@Override
	public Optional<MemberProjection> findById(Long memberId) {
		return springDataMemberProjectionRepository.findById(memberId);
	}

	@Override
	public MemberProjection save(MemberProjection memberProjection) {
		return springDataMemberProjectionRepository.save(memberProjection);
	}
}
