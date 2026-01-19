package com.remitro.member.domain.token.repository;

import java.util.Optional;

import com.remitro.member.domain.token.model.RefreshToken;

public interface RefreshTokenRepository {

	Optional<RefreshToken> findByMemberId(Long memberId);

	void save(RefreshToken refreshToken);

	void deleteByMemberId(Long memberId);
}
