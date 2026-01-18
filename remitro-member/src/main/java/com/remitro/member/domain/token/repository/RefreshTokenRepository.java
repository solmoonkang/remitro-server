package com.remitro.member.domain.token.repository;

import com.remitro.member.domain.token.model.RefreshToken;

public interface RefreshTokenRepository {

	void save(RefreshToken refreshToken);

	void deleteByMemberId(Long memberId);
}
