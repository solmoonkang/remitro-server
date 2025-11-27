package com.remitro.auth.application.service;

import static com.remitro.common.infrastructure.util.constant.JwtClaimsConstant.*;
import static com.remitro.common.infrastructure.util.constant.RedisConstant.*;

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
import com.remitro.common.contract.member.MemberCredentialsResponse;
import com.remitro.common.infrastructure.error.exception.UnauthorizedException;
import com.remitro.common.infrastructure.error.model.ErrorMessage;

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
		final MemberCredentialsResponse memberCredentialsResponse = memberFeignClient.findAuthInfo(
			loginRequest.email()
		);

		if (!passwordEncoder.matches(loginRequest.password(), memberCredentialsResponse.hashedPassword())) {
			throw new UnauthorizedException(ErrorMessage.INVALID_PASSWORD);
		}

		final String accessToken = generateAccessToken(memberCredentialsResponse);
		final String refreshToken = generateRefreshToken(memberCredentialsResponse);
		tokenRepository.saveToken(createRefreshToken(memberCredentialsResponse.memberId(), refreshToken));

		return new TokenResponse(accessToken, refreshToken);
	}

	@Transactional
	public TokenResponse reissueTokens(String refreshToken) {
		final Claims claims = jwtProvider.parseClaims(refreshToken);
		final MemberCredentialsResponse memberCredentialsResponse = memberFeignClient.findAuthInfo(
			claims.get(CLAIM_MEMBER_EMAIL, String.class)
		);

		tokenRepository.findTokenByMemberId(memberCredentialsResponse.memberId())
			.orElseThrow(() -> new UnauthorizedException(ErrorMessage.INVALID_TOKEN));

		final String newAccessToken = generateAccessToken(memberCredentialsResponse);
		final String newRefreshToken = generateRefreshToken(memberCredentialsResponse);
		tokenRepository.saveToken(createRefreshToken(memberCredentialsResponse.memberId(), newRefreshToken));

		return TokenMapper.toTokenResponse(newAccessToken, newRefreshToken);
	}

	private String generateAccessToken(MemberCredentialsResponse memberCredentialsResponse) {
		return jwtProvider.generateAccessToken(
			memberCredentialsResponse.memberId(),
			memberCredentialsResponse.email(),
			memberCredentialsResponse.nickname()
		);
	}

	private String generateRefreshToken(MemberCredentialsResponse memberCredentialsResponse) {
		return jwtProvider.generateRefreshToken(
			memberCredentialsResponse.memberId(),
			memberCredentialsResponse.email()
		);
	}

	private RefreshToken createRefreshToken(Long memberId, String refreshToken) {
		return new RefreshToken(memberId, refreshToken, REFRESH_TOKEN_TTL);
	}
}
