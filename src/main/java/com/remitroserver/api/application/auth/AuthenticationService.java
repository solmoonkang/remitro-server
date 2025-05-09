package com.remitroserver.api.application.auth;

import static com.remitroserver.global.common.util.JwtConstant.*;
import static com.remitroserver.global.error.model.ErrorMessage.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitroserver.api.application.member.MemberReadService;
import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.api.domain.member.repository.TokenRepository;
import com.remitroserver.api.dto.member.request.LoginRequest;
import com.remitroserver.api.dto.member.response.LoginResponse;
import com.remitroserver.global.auth.token.JwtProvider;
import com.remitroserver.global.common.util.PasswordValidator;
import com.remitroserver.global.error.exception.UnauthorizedException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {

	private final PasswordValidator passwordValidator;
	private final JwtProvider jwtProvider;
	private final TokenRepository tokenRepository;
	private final MemberReadService memberReadService;

	@Transactional
	public LoginResponse loginMember(LoginRequest loginRequest) {
		final Member member = memberReadService.getMemberByEmail(loginRequest.email());

		passwordValidator.validatePasswordMatches(loginRequest.password(), member.getPassword());

		final String accessToken = jwtProvider.generateAccessToken(member.getEmail(), member.getNickname());
		final String refreshToken = jwtProvider.generateRefreshToken(member.getEmail());

		tokenRepository.saveToken(member.getEmail(), refreshToken);

		return new LoginResponse(accessToken, refreshToken);
	}

	@Transactional
	public LoginResponse reissueToken(HttpServletRequest httpServletRequest) {
		final String refreshToken = jwtProvider.extractToken(httpServletRequest, REFRESH_TOKEN_HEADER);

		validateRefreshTokenFormat(refreshToken);

		final String email = jwtProvider.extractEmailFromToken(refreshToken);
		final Member member = memberReadService.getMemberByEmail(email);

		tokenRepository.deleteTokenByEmail(email);

		final String newAccessToken = jwtProvider.generateAccessToken(member.getEmail(), member.getNickname());
		final String newRefreshToken = jwtProvider.generateRefreshToken(member.getEmail());

		tokenRepository.saveToken(member.getEmail(), newRefreshToken);

		return new LoginResponse(newAccessToken, newRefreshToken);
	}

	private void validateRefreshTokenFormat(String refreshToken) {
		if (!jwtProvider.validateToken(refreshToken)) {
			throw new UnauthorizedException(UNAUTHORIZED_REFRESH_TOKEN_ERROR);
		}
	}
}
