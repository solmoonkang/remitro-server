package com.remitro.member.domain.token.repository;

import java.util.Optional;

import com.remitro.member.domain.token.model.RefreshToken;

public interface RefreshTokenRepository {

	Optional<RefreshToken> findByToken(String token);

	void save(RefreshToken refreshToken);

	void delteByMemberId(Long memberId);
}
