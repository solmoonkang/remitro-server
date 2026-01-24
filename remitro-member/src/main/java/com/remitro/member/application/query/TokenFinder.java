package com.remitro.member.application.query;

import org.springframework.stereotype.Component;

import com.remitro.common.error.ErrorCode;
import com.remitro.common.exception.UnauthorizedException;
import com.remitro.member.domain.token.model.RefreshToken;
import com.remitro.member.domain.token.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenFinder {

	private final RefreshTokenRepository refreshTokenRepository;

	public RefreshToken getRefreshTokenByMemberId(Long memberId) {
		return refreshTokenRepository.findByMemberId(memberId)
			.orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_TOKEN, "인증 정보"));
	}
}
