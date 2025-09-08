package com.remitro.member.domain.service.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.auth.model.AuthMember;
import com.remitro.common.auth.provider.JwtProvider;
import com.remitro.member.application.dto.request.LoginRequest;
import com.remitro.member.application.dto.response.TokenResponse;
import com.remitro.member.application.mapper.TokenMapper;
import com.remitro.member.application.validator.MemberValidator;
import com.remitro.member.application.validator.TokenValidator;
import com.remitro.member.domain.model.Member;
import com.remitro.member.domain.service.MemberReadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final JwtProvider jwtProvider;
	private final MemberValidator memberValidator;
	private final TokenValidator tokenValidator;
	private final MemberReadService memberReadService;

	@Transactional
	public TokenResponse loginMember(LoginRequest loginRequest) {
		final Member member = memberReadService.findMemberByEmail(loginRequest.email());
		memberValidator.validateLoginPassword(loginRequest.password(), member.getPassword());

		final String accessToken = generateAccessToken(member);
		final String refreshToken = generateRefreshToken(member);
		member.updateRefreshToken(refreshToken);

		return new TokenResponse(accessToken, refreshToken);
	}

	@Transactional
	public TokenResponse reissueTokens(String refreshToken) {
		tokenValidator.validateTokenFormat(refreshToken);
		final AuthMember authMember = jwtProvider.extractAuthMemberFromToken(refreshToken);

		final Member member = memberReadService.findMemberByEmail(authMember.email());
		tokenValidator.validateTokenExistence(member.getRefreshToken(), refreshToken);

		final String newAccessToken = generateAccessToken(member);
		final String newRefreshToken = generateRefreshToken(member);
		member.updateRefreshToken(newRefreshToken);

		return TokenMapper.toTokenResponse(newAccessToken, newRefreshToken);
	}

	private String generateAccessToken(Member member) {
		return jwtProvider.generateAccessToken(member.getId(), member.getEmail(), member.getNickname());
	}

	private String generateRefreshToken(Member member) {
		return jwtProvider.generateRefreshToken(member.getEmail());
	}
}
