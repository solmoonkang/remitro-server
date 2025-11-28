package com.remitro.auth.application.service;

import static com.remitro.common.util.constant.JwtClaimsConstant.*;
import static com.remitro.common.util.constant.RedisConstant.*;

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
import com.remitro.common.contract.MemberAuthInfo;
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
	public TokenResponse loginMember(LoginRequest loginRequest) {
		final MemberAuthInfo memberAuthInfo = memberFeignClient.findAuthInfo(
			loginRequest.email()
		);

		if (!passwordEncoder.matches(loginRequest.password(), memberAuthInfo.hashedPassword())) {
			throw new UnauthorizedException(ErrorMessage.INVALID_PASSWORD);
		}

		final String accessToken = generateAccessToken(memberAuthInfo);
		final String refreshToken = generateRefreshToken(memberAuthInfo);
		tokenRepository.saveToken(createRefreshToken(memberAuthInfo.memberId(), refreshToken));

		return new TokenResponse(accessToken, refreshToken);
	}

	@Transactional
	public TokenResponse reissueTokens(String refreshToken) {
		final Claims claims = jwtProvider.parseClaims(refreshToken);
		final MemberAuthInfo memberAuthInfo = memberFeignClient.findAuthInfo(
			claims.get(CLAIM_MEMBER_EMAIL, String.class)
		);

		tokenRepository.findTokenByMemberId(memberAuthInfo.memberId())
			.orElseThrow(() -> new UnauthorizedException(ErrorMessage.INVALID_TOKEN));

		final String newAccessToken = generateAccessToken(memberAuthInfo);
		final String newRefreshToken = generateRefreshToken(memberAuthInfo);
		tokenRepository.saveToken(createRefreshToken(memberAuthInfo.memberId(), newRefreshToken));

		return TokenMapper.toTokenResponse(newAccessToken, newRefreshToken);
	}

	private String generateAccessToken(MemberAuthInfo memberAuthInfo) {
		return jwtProvider.generateAccessToken(
			memberAuthInfo.memberId(),
			memberAuthInfo.email(),
			memberAuthInfo.nickname()
		);
	}

	private String generateRefreshToken(MemberAuthInfo memberAuthInfo) {
		return jwtProvider.generateRefreshToken(
			memberAuthInfo.memberId(),
			memberAuthInfo.email()
		);
	}

	private RefreshToken createRefreshToken(Long memberId, String refreshToken) {
		return new RefreshToken(memberId, refreshToken, REFRESH_TOKEN_TTL);
	}
}
