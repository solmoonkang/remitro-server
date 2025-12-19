package com.remitro.auth.application.service;

import static com.remitro.auth.infrastructure.constant.RedisConstant.*;
import static com.remitro.common.security.AuthenticationConstant.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.auth.application.dto.request.LoginRequest;
import com.remitro.auth.application.dto.response.TokenResponse;
import com.remitro.auth.application.mapper.TokenMapper;
import com.remitro.auth.domain.model.RefreshToken;
import com.remitro.auth.domain.repository.TokenRepository;
import com.remitro.auth.infrastructure.client.MemberFeignClient;
import com.remitro.auth.infrastructure.security.JwtProvider;
import com.remitro.common.auth.MemberAuthInfo;
import com.remitro.common.error.exception.UnauthorizedException;
import com.remitro.common.error.model.ErrorMessage;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final JwtProvider jwtProvider;
	private final PasswordEncoder passwordEncoder;
	private final MemberFeignClient memberFeignClient;
	private final TokenRepository tokenRepository;

	@Transactional
	public TokenResponse loginMember(String deviceId, LoginRequest loginRequest) {
		final MemberAuthInfo memberAuthInfo = memberFeignClient.findAuthInfo(loginRequest.email());

		if (!passwordEncoder.matches(loginRequest.password(), memberAuthInfo.hashedPassword())) {
			throw new UnauthorizedException(ErrorMessage.INVALID_PASSWORD);
		}

		tokenRepository.revokeByMemberAndDevice(memberAuthInfo.memberId(), deviceId);

		final String accessToken = generateAccessToken(memberAuthInfo);
		final String refreshToken = generateRefreshToken(memberAuthInfo);
		tokenRepository.save(
			createRefreshToken(memberAuthInfo.memberId(), refreshToken, deviceId)
		);

		return new TokenResponse(accessToken, refreshToken);
	}

	@Transactional
	public TokenResponse reissueTokens(String authorizationHeader) {
		final String refreshToken = jwtProvider.extractToken(authorizationHeader);
		final Claims claims = jwtProvider.parseClaims(refreshToken);

		final RefreshToken savedToken = tokenRepository.findByToken(refreshToken)
			.orElseThrow(() -> new UnauthorizedException(ErrorMessage.INVALID_TOKEN));

		tokenRepository.revoke(refreshToken);

		final MemberAuthInfo memberAuthInfo = memberFeignClient.findAuthInfo(
			claims.get(CLAIM_MEMBER_EMAIL, String.class)
		);

		final String newAccessToken = generateAccessToken(memberAuthInfo);
		final String newRefreshToken = generateRefreshToken(memberAuthInfo);
		tokenRepository.save(
			createRefreshToken(memberAuthInfo.memberId(), newRefreshToken, savedToken.deviceId())
		);

		return TokenMapper.toTokenResponse(newAccessToken, newRefreshToken);
	}

	private String generateAccessToken(MemberAuthInfo memberAuthInfo) {
		return jwtProvider.generateAccessToken(
			memberAuthInfo.memberId(),
			memberAuthInfo.email(),
			memberAuthInfo.nickname(),
			memberAuthInfo.role()
		);
	}

	private String generateRefreshToken(MemberAuthInfo memberAuthInfo) {
		return jwtProvider.generateRefreshToken(
			memberAuthInfo.memberId(),
			memberAuthInfo.email()
		);
	}

	private RefreshToken createRefreshToken(Long memberId, String refreshToken, String deviceId) {
		return new RefreshToken(memberId, refreshToken, deviceId, false, REFRESH_TOKEN_TTL);
	}
}
